/*
This JS is for the page where we show the randomly chosen result
*/

//Have Resturant class for easy access and storage
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

//get the array of liked resturants from local storage  and then convert it to an array of resturants
var str = localStorage.getItem("liked");
const likedResturants = [];
function strToArrRes(str) {
  const resturants = str.split("||");
  for (let i = 0; i < resturants.length - 1; i++) {
    const attributes = resturants[i].split(",,,");
    likedResturants.push(
      new Resturant(
        attributes[0],
        attributes[1],
        attributes[2],
        attributes[3],
        attributes[4],
        attributes[5],
        attributes[6]
      )
    );
  }
}

//This is for the first random selection of resturants need this to make sure when we select generate new resturant we get a different and not the same one
var randomRes = -1;
strToArrRes(str);
function randomResturant() {
  return Math.floor(Math.random() * likedResturants.length);
}

//This function selects a resturant and displays it as the choice
function selectResturant() {
  var index = randomResturant();
  while (likedResturants.length > 1 && index == randomRes) {
    index = randomResturant();
  }
  randomRes = index;
  $(".photo img").attr("src", likedResturants[index].image);
  var info = $(".content p");
  info[0].innerHTML = likedResturants[index].name;
  info[2].innerHTML = likedResturants[index].plevel;
  info[3].innerHTML = likedResturants[index].rating;
  info[4].innerHTML = likedResturants[index].distance;
  info[1].innerHTML = likedResturants[index].address;
  info[5].innerHTML = likedResturants[index].open;
}

selectResturant();

$(".gennew").on("click", selectResturant);
