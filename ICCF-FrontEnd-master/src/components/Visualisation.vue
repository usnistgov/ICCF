//../src/components/Visualisation.vue

    <template>
              <chart :chart-data="datacollection"></chart>
    </template>
    <script>
    import Chart from "./../Chart.js";
     import axios from 'axios';
    import $ from 'jquery'
    export default {
      
      components: {
        Chart
      },

      
      data() {
        return {
          datacollection: null
        };
      },
mounted() {
        this.fillData();
  },
      methods: {
        fillData() {
        var xarray = [];
        var yarray = [];

        var z = [1,2,3];
        var i = 0;
            axios
      .get('http://localhost:8087/api/interactions')
      .then(response => {
       for (i=0; i< response.data.length ; i++)
        {
          xarray[i]=parseInt(response.data[i].entryId);
          yarray[i]=parseInt(response.data[i].DataField);
        }

console.log(xarray);
console.log(yarray);
console.log(z);

      })
      .catch(error => {
        this.errored = true
      })
      .finally(() => this.loading = false)
if ( xarray instanceof Array ) {console.log("hello")}else{console.log("no")}
          this.datacollection = {
            labels: xarray,
            datasets: [
              {
                label: "DriveCycle",
                backgroundColor: "#f87979",
                data: yarray
              }
            ]
          };
        },
      }
    };
    </script>
    <style>
    </style>