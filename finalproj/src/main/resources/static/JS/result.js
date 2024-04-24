//This is for the like and dislike page
//have a resturant class for easy use and storage
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

//this puts all the divs on different z layers so they are stacked on top of each other
function displayResults() {
  const elements = document.querySelectorAll(".content");
  for (let i = 0; i < elements.length; i++) {
    elements[i].style.zIndex = 20 - i;
  }
}

//keep track of the like resturants and the disliked one
var index = 1;
const likedResturants = [];
const disResturants = [];

//clear local storage incase we want to change answers
displayResults();
localStorage.clear();

//grabs all the information from the div and creates a resturant object
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

//adds the resturant to the liked list and removes the div from the stack
function like() {
  const res = makeResturant();
  likedResturants.push(res);
  $("#" + index).remove();
  index++;
}
//adds the resturant to the disliked list and removes the div from the stack
function dislike() {
  const res = makeResturant();
  disResturants.push(res);
  $("#" + index).remove();
  index++;
}

$(".like").on("click", like);
$(".dislike").on("click", dislike);

//stores the list of liked resturants into local storage
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
