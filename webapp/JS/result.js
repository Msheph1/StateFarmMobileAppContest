//we need to take in a list of data
//create resturants objects from it
//create divs for everything and stack them
//then like/dislike removes them
//store list of resturants in local storage

class Resturant {
  constructor(name, plevel, rating, distance, address, open, image) {
    this.name = name;
    this.plevel = plevel;
    this.rating = rating;
    this.distance = distance;
    this.address = address;
    this.open = open;
    this.image = image;
  }
}

function displayResults() {
  const elements = document.querySelectorAll(".content");
  for (let i = 0; i < elements.length; i++) {
    elements[i].style.zIndex = 20 - i;
  }
}
var index = 1;
const likedResturants = [];
const disResturants = [];

displayResults();
localStorage.clear();

function makeResturant() {
  var info = $("#" + index + " p");
  var img = $("#" + index + " img").attr("src");
  var resname = info[1].innerHTML;
  var plevel = info[3].innerHTML;
  var rating = info[5].innerHTML;
  var distance = info[7].innerHTML;
  var address = info[9].innerHTML;
  var open = info[11].innerHTML;
  for (let i = 1; i < info.length; i = i + 2) {
    console.log(info[i].innerHTML);
  }
  console.log(img);
  return new Resturant(resname, plevel, rating, distance, address, open, img);
}

function like() {
  const res = makeResturant();
  likedResturants.push(res);
  $("#" + index).remove();
  index++;
}
function dislike() {
  const res = makeResturant();
  disResturants.push(res);
  $("#" + index).remove();
  index++;
}

$(".like").on("click", like);
$(".dislike").on("click", dislike);

function storeResturants(arr) {
  var str = "";
  for (let i = 0; i < arr.length; i++) {
    str +=
      arr[i].name +
      ",,," +
      arr[i].plevel +
      ",,," +
      arr[i].rating +
      ",,," +
      arr[i].distance +
      ",,," +
      arr[i].address +
      ",,," +
      arr[i].open +
      ",,," +
      arr[i].image +
      "||";
  }
  localStorage.setItem("liked", str);
}

$(".nextp").on("click", function () {
  storeResturants(likedResturants);
});
