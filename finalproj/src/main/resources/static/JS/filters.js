/*
This is for when we select use the computers current location in our form instead of having the user enter longitude and latitude coordinates
*/

function getlocation() {
  
  function success(position) {
    $("#lati").val(position.coords.latitude);
    $("#longi").val(position.coords.longitude);
  }
  function error() {
    alert("Location services need to be turned on for that action");
  }
  navigator.geolocation.getCurrentPosition(success, error ,{enableHighAccuracy: true, maximumAge: 10000});
}

function validate(){
formsub();

}


function formsub()
{
  
  $(".loading").show();
}

$(".locbtn").on("click", getlocation);




