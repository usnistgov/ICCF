<template>
<div id="chart-container">
  FusionCharts 
  </div>
</template>
]
<script>


  
  // data() {
  //   return {

    import axios from 'axios';
   // import Vue from `vue`;
import VueFusionCharts from 'vue-fusioncharts';

// import FusionCharts modules and resolve dependency
import FusionCharts from 'fusioncharts';
import Charts from 'fusioncharts/fusioncharts.charts';



    var mycpu = 0;
    var finalcpu = 0;

FusionCharts.ready(function() {
  var cSatScoreChart = new FusionCharts({

    type: 'angulargauge',
    id: "myGauge",
    renderAt: 'chart-container',
    width: '400',
    height: '380',
    dataFormat: 'json',
    dataSource: {
      "chart": {
        "theme": "fusion",
        "caption": "CPU USAGE",
        "captionFont": "Arial",
        "captionFontColor": "#333333",
        "manageresize": "1",
        "origw": "320",
        "origh": "320",
        "tickvaluedistance": "-10",
        "bgcolor": "#FFFFFF",
        "upperlimit": "100",
        "lowerlimit": "0",
        "basefontcolor": "#FFFFFF",
        "majortmnumber": "9",
        "majortmcolor": "#FFFFFF",
        "majortmheight": "8",
        "majortmthickness": "5",
        "minortmnumber": "5",
        "minortmcolor": "#FFFFFF",
        "minortmheight": "3",
        "minortmthickness": "2",
        "pivotradius": "10",
        "pivotbgcolor": "#000000",
        "pivotbordercolor": "#FFFFFF",
        "pivotborderthickness": "2",
        "tooltipbordercolor": "#FFFFFF",
        "tooltipbgcolor": "#333333",
        "gaugeouterradius": "115",
        "gaugestartangle": "240",
        "gaugeendangle": "-60",
        "decimals": "0",
        "placevaluesinside": "1",
        "pivotfillmix": "",
        "showpivotborder": "1",
        "annrenderdelay": "0",
        "gaugeoriginx": "160",
        "gaugeoriginy": "160",
        "showborder": "0"
      },
      "dials": {
        "dial": [{
          "value": "65",
          "bgcolor": "000000",
          "bordercolor": "#FFFFFF",
          "borderalpha": "100",
          "basewidth": "4",
          "topwidth": "4",
          "borderthickness": "2",
          "valuey": "260"
        }]
      },


      
      "annotations": {
        "groups": [{
          "x": "160",
          "y": "160",
          "items": [{
            "type": "circle",
            "radius": "130",
            "fillasgradient": "1",
            "fillcolor": "#4B4B4B,#AAAAAA",
            "fillalpha": "100,100",
            "fillratio": "95,5"
          }, {
            "type": "circle",
            "x": "0",
            "y": "0",
            "radius": "120",
            "showborder": "1",
            "bordercolor": "cccccc",
            "fillasgradient": "1",
            "fillcolor": "#ffffff,#000000",
            "fillalpha": "50,100",
            "fillratio": "1,99"
          }]
        }, {
          "x": "160",
          "y": "160",
          "showbelow": "0",
          "scaletext": "1",
          "items": [{
            "type": "text",
            "y": "100",
            "label": "Khalid's MacBook Pro",
            "fontcolor": "#FFFFFF",
            "fontsize": "8",
            "bold": "1"
          }]
        }]
      }
    },
 "events": {




     




      "initialized": function(evtObj, argObj) {
        var scoreArray = [],
          score,
          speedScore,
          flag,
          cv,
          diff;
        evtObj.sender.resetInterval = setInterval(function() {
          var i,
            percent,
            num = 30;
          score = 40 +
            parseInt(Math.floor(Math.random() * 170));


       axios
      .get('http://localhost:8099/products')
      .then(response => {
        if((response.data).toString() == "NaN")
        {
         cv= 0

        } else{
        cv = parseInt(100*(response.data))
        }
       // console.log(parseInt(mycpu))
      })




 console.log("cpu usage "+cv);
       //     console.log("score "+score);


      //      console.log("random "+  parseInt(Math.floor(Math.random() * 170)));
          if (!speedScore) {
            speedScore = cv;
          }
       //   diff = (score - speedScore);



          // for (i = 0; i < 5; i++) {
          //   percent = num - (i * 5);
          //   scoreArray[i] = diff * (percent / 100);
          // }
          // flag = 0;

        }, 2000);
        evtObj.sender.changeInterval = setInterval(function() {
        //  if (speedScore && diff) {
            speedScore = cv;

          //     console.log("speedScore "+speedScore);
         //   flag += 1;
            FusionCharts.items["myGauge"].feedData("value=" + speedScore);

     //     }
        }, 2000);
      },
      "disposed": function(evtObj, argObj) {
        clearInterval(evtObj.sender.resetInterval);
        clearInterval(evtObj.sender.changeInterval);
      }
    }




  }).render();


}
        
  //     }
   
   
   
  //  };
  // },






// export default {  
//   props: ["highlights"],
//   components: {},
























































//   created() {
//  setInterval(this.fillData, 2000)
//   },
//   beforeDestroy() {
//     if (this.interval) {
//       clearIntervall(this.interval)
//       this.interval = undefined
//     }
//   },
// mounted() {
//         cSatScoreChart.render();
//          setInterval(this.fillData(),2000);
         
//   },




      // methods: {
      //   fillData() {

      //   },
      // },

//   watch: {
//     highlights: {
//       handler: function() {
//         this.datasource.colorrange.color[0].maxvalue = (parseInt(mycpu).toString());
//         this.datasource.colorrange.color[1].minvalue = (parseInt(mycpu).toString());
//         this.datasource.annotations.groups[0].items[0].text = (parseInt(mycpu).toString());
//       },
//       deep: true
//     }
//   }
);
</script>


