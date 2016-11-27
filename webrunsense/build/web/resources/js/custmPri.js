$("#:f_reponse:id_gph").bind('jqplotMouseMove', function(ev, gridpos, datapos, neighbor, plot) {
    if (neighbor) {
        $(".jqplot-highlighter-tooltip").html("" + plot.axes.xaxis.ticks[neighbor.pointIndex][1] + ", " + datapos.yaxis.toFixed(2) + " Oi");
    }
});


