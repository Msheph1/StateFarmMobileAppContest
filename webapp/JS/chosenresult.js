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

strToArrRes(str);
function randomResturant() {
  return Math.floor(Math.random() * likedResturants.length);
}
function selectResturant() {
  var index = randomResturant();
  $(".photo img").attr("src", likedResturants[index].image);
  var info = $(".content p");
  info[1].innerHTML = likedResturants[index].name;
  info[3].innerHTML = likedResturants[index].plevel;
  info[5].innerHTML = likedResturants[index].rating;
  info[7].innerHTML = likedResturants[index].distance;
  info[9].innerHTML = likedResturants[index].address;
  info[11].innerHTML = likedResturants[index].open;
}

selectResturant();

$(".gennew").on("click", selectResturant);
