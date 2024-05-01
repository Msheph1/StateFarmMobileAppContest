/*
This is for the list of all liked resturants
*/

//resturant class for easy access and storage
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

//keep all resturants in an array of resturant objects
var str = localStorage.getItem("liked");
const likedResturants = [];
function strToArrRes(str) {
  if (str != "") {
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
}

strToArrRes(str);

//this creates and displays the list for every resturant in the list
function createList() {
  for (let i = 1; i <= likedResturants.length; i++) {
    $(".listcont").append("<div class=" + "'" + "row content " + i + "'" + ">");
    //h1
    $("." + i).append(
      "<h1 class=" + "'" + "numlabel" + "'" + ">" + i + "</h1>"
    );
    //details
    $("." + i).append(
      "<div class=" + "'" + "col-md col-lg-6 details" + "'" + ">"
    );
    //detail resname
    $("." + i + " .details").append(
      "<div class=" + "'" + "resname para" + "'" + ">"
    );
    $("." + i + " .resname").append("<p>Resturant Name:</p>");
    $("." + i + " .resname").append(
      "<p> " + likedResturants[i - 1].name + "</p>"
    );
    //detail pricelevel
    $("." + i + " .details").append(
      "<div class=" + "'" + "pricelevel para" + "'" + ">"
    );
    $("." + i + " .pricelevel").append("<p>Price Level:</p>");
    $("." + i + " .pricelevel").append(
      "<p> " + likedResturants[i - 1].plevel + "</p>"
    );
    //detail ratings
    $("." + i + " .details").append(
      "<div class=" + "'" + "rating para" + "'" + ">"
    );
    $("." + i + " .rating").append("<p>Rating:</p>");
    $("." + i + " .rating").append(
      "<p> " + likedResturants[i - 1].rating + "</p>"
    );
    //detail distance
    $("." + i + " .details").append(
      "<div class=" + "'" + "distance para" + "'" + ">"
    );
    $("." + i + " .distance").append("<p>Distance:</p>");
    $("." + i + " .distance").append(
      "<p> " + likedResturants[i - 1].distance + "</p>"
    );
    //detail address
    $("." + i + " .details").append(
      "<div class=" + "'" + "address para" + "'" + ">"
    );
    $("." + i + " .address").append("<p>Address:</p>");
    $("." + i + " .address").append(
      "<p> " + likedResturants[i - 1].address + "</p>"
    );
    //detail open
    $("." + i + " .details").append(
      "<div class=" + "'" + "open para" + "'" + ">"
    );

    $("." + i + " .open").append("<p> " + likedResturants[i - 1].open + "</p>");

    //photo area
    $("." + i).append(
      "<div class=" + "'" + "col-md-12 col-lg-6 photo" + "'" + ">"
    );
    $("." + i + " .photo").append(
      "<img src=" + "'" + likedResturants[i - 1].image + "'" + ">"
    );
  }
}

createList();

//uses selction sort to sort by distance
function sortByDistance() {
  for (let i = 0; i < likedResturants.length; i++) {
    var min = i;
    for (let j = i + 1; j < likedResturants.length; j++) {
      if (likedResturants[j].distance < likedResturants[min].distance) {
        min = j;
      }
    }
    var temp = likedResturants[i];
    likedResturants[i] = likedResturants[min];
    likedResturants[min] = temp;
  }
  redoList();
}

//uses selction sort to sort by rating
function sortByRating() {
  for (let i = 0; i < likedResturants.length; i++) {
    var max = i;
    for (let j = i + 1; j < likedResturants.length; j++) {
      if (likedResturants[j].rating > likedResturants[max].rating) {
        max = j;
      }
    }
    var temp = likedResturants[i];
    likedResturants[i] = likedResturants[max];
    likedResturants[max] = temp;
  }
  redoList();
}

//uses selction sort to sort by price
function sortByPrice() {
  for (let i = 0; i < likedResturants.length; i++) {
    var min = i;
    for (let j = i + 1; j < likedResturants.length; j++) {
      if (likedResturants[j].plevel < likedResturants[min].plevel) {
        min = j;
      }
    }
    var temp = likedResturants[i];
    likedResturants[i] = likedResturants[min];
    likedResturants[min] = temp;
  }
  redoList();
}

$(".distance").on("click", sortByDistance);
$(".rating").on("click", sortByRating);
$(".price").on("click", sortByPrice);

//delete the entire list in html and replace it with the newely sorted resturant array.
function redoList() {
  $(".listcont").html("");
  createList();
}

if(likedResturants.length == 0)
{
  $(".noRes").text("No Liked Restaurants");

}
