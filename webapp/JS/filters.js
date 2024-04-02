function getlocation() {
  console.log("clicked");
  function success(position) {
    console.log(position);
    console.log(position.coords.latitude);
    console.log(position.coords.longitude);
    $("#lati").val(position.coords.latitude);
    $("#longi").val(position.coords.longitude);
  }
  function error() {
    alert("Location services need to be turned on for that action");
  }
  navigator.geolocation.getCurrentPosition(success, error);
}

$(".locbtn").on("click", getlocation);
