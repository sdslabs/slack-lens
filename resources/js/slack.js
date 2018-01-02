const botImg = "https://slack-files2.s3-us-west-2.amazonaws.com/avatars/2015-10-09/12167763254_75a2d1841d07a114dcc3_36.jpg";
function w3_open() {
  document.getElementById("mySidebar").style.right = "0";
}

function w3_close() {
  document.getElementById("mySidebar").style.right = "-30%";
}

function fetchMessage(path, parameter, query) {
  var url = "/v1/" + path + "?" + parameter + "=" + query;
  console.log(url);
  loadDoc(url, "thread");
}

function check() {
  document.getElementById(active).style.backgroundColor = "#6698C8";
}
check();


var input = document.getElementById('search');
loadDoc("/v1/channel?channel=" + active, "mainview");
function decodeHtml(html) {
  var txt = document.createElement("textarea");
  txt.innerHTML = html;
  return txt.value;
}

function time_stamp(stamp) {
  return (new Date(stamp)).toString().split("G")[0];
}

function eventRefresh() {
  $(".ref").click(function (x) {
    console.log(x.target.innerHTML);
    loadDoc("usermes?person=" + x.target.innerHTML.match(/(\d|\w)+/g) + "&channel=" + active, "thread");
  });
}

function loadDoc(url, loadWhere) {
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function () {
    if (this.readyState == 4 && this.status == 200) {
      var tmp = JSON.parse(decodeHtml(this.responseText)).messages;
      if (loadWhere == "thread") {
        document.getElementById("sidebar").innerHTML = null;
        w3_open();
      }
      else if (loadWhere == "mainview")
        parent = document.getElementsByClassName("message-history")[0].innerHTML = null;

      for (x in tmp) {
        //a+="<div class='message'>";
        var messageDiv = document.createElement("div");
        messageDiv.setAttribute("class", "message");
        if (tmp[x]._source.replies) {
          var threadLink = document.createElement("a");
          threadLink.setAttribute("onclick", "return fetchMessage('thread', 'thread_ts' , " + tmp[x]._source.ts + ")");
          threadLink.setAttribute("href", "#");
          threadLink.setAttribute("style", "position:absolute;right:5%");
          threadLink.textContent = "thread";
          messageDiv.appendChild(threadLink);
        }

        var userImage = document.createElement("img");
        userImage.setAttribute("class", "message_profile-pic");
        userImage.setAttribute("src", tmp[x]._source.image_48 || botImg);
        messageDiv.appendChild(userImage);

        var username = document.createElement("a");
        username.setAttribute("href", "#");
        username.setAttribute("class", "message_username ref");
        username.textContent = tmp[x]._source.name || "Bot";
        messageDiv.appendChild(username);

        var tsElement = document.createElement("span");
        tsElement.setAttribute("class", "message_timestamp");
        tsElement.setAttribute("id", tmp[x]._source.ts);
        tsElement.textContent = time_stamp(tmp[x]._source.ts * 1000);
        messageDiv.appendChild(tsElement);

        var message = document.createElement("span");
        message.setAttribute("class", "message_content");
        message.innerHTML = tmp[x]._source.text;
        messageDiv.appendChild(message);

        if (tmp[x]._source.file) {
          var file = document.createElement("img");
          file.setAttribute("src", tmp[x]._source.file.thumb_360);
          var br = document.createElement("br");

          messageDiv.appendChild(br);
          messageDiv.appendChild(file);
        }

        if (loadWhere == "thread") {
          document.getElementById("sidebar").appendChild(messageDiv);
          w3_open();
        }
        else if (loadWhere == "mainview")
          parent = document.getElementsByClassName("message-history")[0].appendChild(messageDiv);
      }
      eventRefresh();
    }
  };
  if (loadWhere == "logout") {
    window.location.href = "/v1/slack-lens";
  }
  xhttp.open("GET", url, true);
  xhttp.send();
}

input.addEventListener('keypress', function (e) {
  if ((e.which || e.keyCode) == 13) {
    if (!/(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\d\d/.test(input.value)) {
      alert("Enter correct format of date");
      return;
    }
    input.blur();
    loadDoc("data?date=" + input.value + "&length=" + document.getElementById("number").value + "&channel=" + active, "mainview");
    input.value = "";
  }
});

function dropdownHandle() {
  let display = document.getElementById("dropdown").style.display;
  if ("inline-block" == display) {
    document.getElementById("dropdown").style.display = "none";
  } else if (display == "none" || display == "") {
    document.getElementById("dropdown").style.display = "inline-block";
  }
}

// Close the dropdown if the user clicks outside of it
window.onclick = function (event) {
  if (!(event.target.matches('.menu') || event.target.matches('#dropdown') || event.target.matches('#logout'))) {
    document.getElementById("dropdown").style.display = "none";
  }

}

document.getElementById("dropdown").onclick = function logout(e) {
  e.preventDefault();
  let token = document.cookie.split("token=")[1].split("; ")[0];
  document.cookie = "token=;expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
  loadDoc("/v1/logout?token=" + token, "logout");
}
