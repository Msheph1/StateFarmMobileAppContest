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
  var lati = document.getElementById("lati").value;
  var longi = document.getElementById("lngi").value;
  var dist = document.getElementById("dist").value;
    if (isNaN(lati) || lati > 180 ||  lati < -180) {
    alert("latitude is invalid");
    return false;
}  else if (isNaN(longi) || longi > 180 ||  longi < -180) {
    alert("longitude is invalid");
    return false;
}  else if (isNaN(dist) || dist > 50000 ||  dist < 0) {
  alert("distance is invalid");
  return false;
}
formsub();
return true;

}


function formsub()
{
  
  $(".loading").show();
}

$(".locbtn").on("click", getlocation);




