const NODES_INTERVAL = 500;
const DEFAULT_NODES_FONT_SIZE = 48;

window.setVisData = function (jsonString) {
  clearNodes();
  clearEdges();

  const data = JSON.parse(jsonString);

  data.nodes.forEach((n, i) => {
    n.shape = "dot";
  });

  data.edges.forEach((n, i) => {
    n.label = "" + (n.width || 1);
  });

  window.visNodes = new vis.DataSet(data.nodes);
  window.visEdges = new vis.DataSet(data.edges);

  window.visNetwork.setData({ nodes: window.visNodes, edges: window.visEdges });
  populateLegend(data);

  // options
  window.setVisNodeColor(data.startNode, window.caaOptions.startNodeColor);
  window.setVisOptions();

  // happy, sad & start node
  window.setVisNodesColor(JSON.stringify(data.happyNodes), window.caaOptions.happyNodesColor);
  window.setVisNodesColor(JSON.stringify(data.sadNodes), window.caaOptions.sadNodesColor);
};

window.setVisOptions = function (options) {
  if (options) {
    window.visOptions = JSON.parse(options);
  }

  if(window.visNetwork && window.visOptions) {
    window.visNetwork.setOptions(window.visOptions);
  }

  if(window.legendNetwork && window.visOptions) {
    var cloneOptions = Object. assign({}, window.visOptions);
    // avoiding to hide nodes labels for the legend
    cloneOptions.nodes.font.size = DEFAULT_NODES_FONT_SIZE;
    window.legendNetwork.setOptions(window.visOptions);
  }
};

window.setCaaOptions = function (options){
  if (options) {
    window.caaOptions = JSON.parse(options);
  }
};

window.setVisNodeColor = function (id, color) {
  if (window.visNodes.get(id)) {
    window.visNodes.update({ id: id, color: { background: color } });
  }
  if (window.legendNodes.get(id)) {
    window.legendNodes.update({ id: id, color: { background: color } });
  }
};

window.setVisNodesColor = function (nodesIds, color) {
  nodesIds = JSON.parse(nodesIds);
  for (let i = 0; i < nodesIds.length; i++) {
    window.setVisNodeColor(nodesIds[i], color);
  }
};

window.animateCaaVertices = function (jsonString) {
  alert("Animating Nodes is Starting. Press OK to continue.");
  return window.animateVisNodes(JSON.parse(jsonString)).then(() => {
    setTimeout(() => {
      alert("Animating Nodes is Finished. Press OK to continue.");
    }, NODES_INTERVAL);
  });
};

window.drawCaaVertices = function (jsonString) {
  const nodes = JSON.parse(jsonString);
  clearNodes();

  for (let counter = 0; counter < nodes.length; counter++) {
    const node = getNode(nodes[counter]);
    window.visNodes.update({
      id: node.id,
      borderWidth: (node.borderWidth ? node.borderWidth : 0.0) + 6.0,
      color: { border: "#000" }
    });
  }
};

window.animateVisEdges = function (jsonString) {
  cleanUp();

  alert("Animating Edges is Starting. Press OK to continue.");
  const edgesHistory = JSON.parse(jsonString);
  const edges = window.visEdges;
  setTimeout(() => {
    let i = 0;
    window.animateVisEdgesIntervalId = setInterval(() => {
      const edge = getEdgeBetweenNodes(
        edgesHistory[i].from,
        edgesHistory[i].to
      );
      edges.update({
        id: edge.id,
        width: (edge.width ? edge.width : 0.0) + 10.0
      });
      window.visNetwork.redraw();

      if (i == edgesHistory.length - 1) {
        clearInterval(window.animateVisEdgesIntervalId);
        alert("Animating Edges Finished. Press OK to continue.");
      }

      i++;
    }, 1000);
  }, 50);
};

window.animateVisNodes = function (nodes) {
  return new Promise((resolve, reject) => {
    let counter = 0;
    const nodeId = setInterval(() => {
      if (counter === 0) {
        clearNodes();
      }
      const node = getNode(nodes[counter]);
      window.visNodes.update({
        id: node.id,
        borderWidth: (node.borderWidth ? node.borderWidth : 0.0) + 6.0,
        color: { border: "#000" }
      });

      const end = nodes.length - 1;
      if (counter >= end) {
        clearInterval(nodeId);
        resolve();
      }

      counter++;
    }, NODES_INTERVAL);
  });
};

window.animateCaaExperiment = function (
  jsonString
) {
  cleanUp();

  const data = JSON.parse(jsonString);

  alert("Animating Caa Experiment is Starting. Press OK to continue");

  let timeCounter = 0;
  let time = 0;

  animateVisNodes(data[0].traversedVertices).then(() => {
    drawCaaIncrements(data[0].increments, window.caaOptions.happyNodesColor, window.caaOptions.sadNodesColor);
  });
  document.querySelector("#notifications").innerText = "Generation #1";

  for (let counter = 1; counter < data.length; counter++) {
    const increments = data[counter].increments;
    const nodes = data[counter].traversedVertices;
    const prevNodes = data[counter - 1].traversedVertices;
    const extraWaitTime = 500;
    time = prevNodes.length * NODES_INTERVAL + extraWaitTime;
    timeCounter += time;

    setTimeout(() => {
      document.querySelector("#notifications").innerText =
        "Generation #" + (counter + 1);

      animateVisNodes(nodes).then(() => {
        drawCaaIncrements(increments, window.caaOptions.happyNodesColor, window.caaOptions.sadNodesColor);
        const end = data.length - 1;
        if (counter >= end) {
          clearInterval(window.animateCaaExperimentIntervalId);
          setTimeout(() => {
            alert("Animating Caa Experiment Finished. Press OK to continue.");
          }, NODES_INTERVAL);
        }
      });
    }, timeCounter);
  }
};

window.drawCaaIncrements = function (
  increments
) {
  const edgesHistory = increments;

  const edges = window.visEdges;
  edgesHistory.forEach((item, i, a) => {
    const edge = getEdgeBetweenNodes(edgesHistory[i].from, edgesHistory[i].to);

    let color;
    if (item.weight > 0) {
      color = { color: window.caaOptions.happyNodesColor };
    } else {
      color = { color: window.caaOptions.sadNodesColor };
    }

    const width = (edge.width ? edge.width : 0.0) + 10.0;
    const backgroundSize = width ? width + 20.0 : 10.0;
    edges.update({
      id: edge.id,
      width,
      color,
      background: {
        enabled: edge.background ? edge.background.enabled : false,
        size: backgroundSize
      }
    });
  });
};

window.drawCaaExperiment = function (
  jsonString
) {
  cleanUp();

  const data = JSON.parse(jsonString);

  for (let counter = 1; counter < data.length; counter++) {
    const increments = data[counter].increments;
    drawCaaIncrements(increments, window.caaOptions.happyNodesColor, window.caaOptions.sadNodesColor);
  }
}

window.setVisEdgesBackground = function (shortestPathsString, enabled) {

  const data = JSON.parse(shortestPathsString);
  const edges = window.visEdges;
  data.forEach((item, i, a) => {
    const edge = getEdgeBetweenNodes(data[i].from, data[i].to);

    edges.update({
      id: edge.id,
      background: { enabled: Boolean(enabled), size: (edge.width ? (edge.width + 20.0) : 10.0) }
    });

  });

};

function clearNodes() {
  const nodes = window.visNodes;
  if (nodes) {
    nodes.get().forEach(function (node) {
      nodes.update({ id: node.id, borderWidth: 0.0 });
    });
  }
}

function clearEdges() {
  clearInterval(window.animateVisEdgesIntervalId);
  clearInterval(window.animateCaaExperimentIntervalId);

  const edges = window.visEdges;
  if (edges) {
    edges.get().forEach(function (edge) {
      edges.update({ id: edge.id, width: 1.0, color: "#000", background: { ...edge.background, size: 10.0 } });
    });
  }
}

function getEdgeBetweenNodes(node1, node2) {
  const fromId = getNode(node1).id;
  const toId = getNode(node2).id;
  const edges = window.visEdges;
  return edges.get().filter(function (edge) {
    return (
      (edge.from === fromId && edge.to === toId) ||
      (edge.from === toId && edge.to === fromId)
    );
  })[0];
}

function getNode(label) {
  const nodes = window.visNodes;
  return nodes.get().filter(function (node) {
    return node.label === label;
  })[0];
}

function clearNotifications() {
  const notifications = window.document.querySelector("#notifications");
  notifications.innerText = "";
}

function cleanUp() {
  clearNodes();
  clearEdges();
  clearNotifications();
}

function populateLegend(data) {
  const legendContainer = document.querySelector("#legend");
  window.legendNetwork = new vis.Network(legendContainer, {}, window.visOptions);
  const step = 200;
  const legendNodes = [
    {
      id: 12312332132,
      x: 0,
      y: 0,
      label: "Legend",
      shape: "text"
    },
    {
      id: data.startNode,
      x: 0,
      y: (1 * step),
      label: "Start node",
      shape: "dot"
    },
    {
      id: data.happyNodes[0],
      x: 0,
      y: (2 * step),
      label: "Happy",
      shape: "dot"
    },
    {
      id: data.sadNodes[0],
      x: 0,
      y: (3 * step),
      label: "Sad",
      shape: "dot"
    },
    {
      id: 089089089,
      x: 0,
      y: (4 * step),
      label: "Traversed",
      shape: "dot",
      borderWidth: 3.0,
      color: {
        border: "black"
      }
    }
  ];

  window.legendNodes = new vis.DataSet(legendNodes);
  window.legendNetwork.setData({ nodes: window.legendNodes });
}

document.addEventListener("DOMContentLoaded", function () {
  var container = document.querySelector("#graph");

  // var options = {
  //   nodes: {
  //     size: 42,
  //     borderWidth: 0.0,
  //     color: {
  //       border: "#A22"
  //     },
  //     font: {
  //       color: "black",
  //       face: "Arvo",
  //       size: 48,
  //       strokeWidth: 1,
  //       strokeColor: "black"
  //     }
  //   },
  //   edges: {
  //     font: {
  //       color: "black",
  //       face: "Arvo",
  //       size: 0
  //     },
  //     color: {
  //       color: "black",
  //       highlight: "#A22"
  //     },
  //     length: 275,
  //     background: {
  //       color: "#ffa500ff"
  //     }
  //   }
  // };

  // window.visOptions = options;

  window.visNetwork = new vis.Network(container, {}, window.visOptions);

  // Below code is for testing locally

  // var data = {
  //   nodes: [
  //     {
  //       id: 1,
  //       label: "HTML5"
  //     },
  //     {
  //       id: 2,
  //       label: "CSS3"
  //     }
  //   ],
  //   edges: [{ id: 0, from: 1, to: 2 }]
  // };
  // window.setVisData(JSON.stringify(data));


});

