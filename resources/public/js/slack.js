const botImg = "https://slack-files2.s3-us-west-2.amazonaws.com/avatars/2015-10-09/12167763254_75a2d1841d07a114dcc3_36.jpg";
function w3_open() {
  document.getElementById("mySidebar").style.right = "0";
}

function w3_close() {
  document.getElementById("mySidebar").style.right = "-30%";
}

function fetchMessage(path, parameter, query) {
  var url = "/v1/" + path + "?" + parameter + "=" + query;
  loadDoc(url, "Thread");
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
  return (new Date(stamp)).toString().split("G")[0]
    .replace(/(\w+) (\w+ [\d]{2} [\d]{4}) (\d{2}:\d{2}):(\d{2})/, "$3 ($2)");
}

document.body.addEventListener('click', function (event) {
  if (event.target.className.split(" ").includes("ref")) {
    let name = event.target.text.replace("@", "");
    loadDoc("usermes?person=" + name + "&channel=" + active, "User", ": " + name);
  }
});

function renderServiceMes(attachments, messageDiv) {
  for( x in attachments){

    let text = attachments[x].text;
    let topic = attachments[x].fallback;
    let image_url = attachments[x].image_url;
    let title_link = attachments[x].title_link;
    let service_icon = attachments[x].service_icon;


    let serviceContainer = document.createElement("div");
    serviceContainer.setAttribute("class", "attachment-container");
    serviceContainer.style.borderLeft = "5px solid #" + attachments[x].color;

    let icon= document.createElement("img");
    icon.setAttribute("src", service_icon);
    icon.setAttribute("class", "service-icon");
    serviceContainer.appendChild(icon);

    let title =document.createElement("span");
    title.textContent = topic;
    serviceContainer.appendChild(title);

    serviceContainer.appendChild(document.createElement("br"));

    let anchor =document.createElement("a");
    anchor.setAttribute("href", title_link);
    anchor.textContent = title_link;
    serviceContainer.appendChild(anchor);

    let img= document.createElement("img");
    img.setAttribute("src", image_url);
    img.setAttribute("class", "service-img");
    serviceContainer.appendChild(img);

    messageDiv.appendChild(serviceContainer);


  }
}


function renderInfo(attachments, messageDiv) {
  for (x in attachments) {
    let fallback = attachments[x].fallback.split('\t');

    // container
    let attachmentContainer = document.createElement("div");
    attachmentContainer.setAttribute("class", "attachment-container");
    attachmentContainer.style.borderLeft = "5px solid #" + attachments[x].color;

    // container-username
    let attachmentUsername = document.createElement("div");
    attachmentUsername.setAttribute("class", "attachment-user-name");
    attachmentUsername.textContent = fallback[0];
    attachmentContainer.appendChild(attachmentUsername);

    // container-user-addr
    let attachmentUserAddr = document.createElement("div");
    attachmentUserAddr.setAttribute("class", "attachment-user-addr");
    attachmentUserAddr.textContent = "Address: " + fallback[8];
    attachmentContainer.appendChild(attachmentUserAddr);


    // container-user-mobile
    let attachmentUserMobile = document.createElement("div");
    attachmentUserMobile.setAttribute("class", "attachment-user-mobile");

    let spanMobile = document.createElement("span");
    spanMobile.innerHTML = "Mobile<br>";
    let mobileAnchor = document.createElement("a");
    mobileAnchor.setAttribute("href", "tel:" + fallback[3]);
    mobileAnchor.textContent = fallback[3];

    attachmentUserMobile.appendChild(spanMobile);
    attachmentUserMobile.appendChild(mobileAnchor);

    attachmentContainer.appendChild(attachmentUserMobile);


    // container-user-email
    let attachmentUserEmail = document.createElement("div");
    attachmentUserEmail.setAttribute("class", "attachment-user-email");

    let spanEmail = document.createElement("span");
    spanEmail.innerHTML = "Email<br>";
    let emailAnchor = document.createElement("a");
    let email = fallback[4].split('>')[0].split('|')[1];
    emailAnchor.setAttribute("href", "mailto:" + email);
    emailAnchor.textContent = email;

    attachmentUserEmail.appendChild(spanEmail);
    attachmentUserEmail.appendChild(emailAnchor);

    attachmentContainer.appendChild(attachmentUserEmail);
    messageDiv.appendChild(attachmentContainer);
  }
}

function renderMessage(tmp, loadWhere) {
  for (x in tmp) {
    var messageDiv = document.createElement("div");
    messageDiv.setAttribute("class", "message");
    if (tmp[x]._source.replies) {
      var threadLink = document.createElement("a");
      threadLink.setAttribute("onclick", "return fetchMessage('thread', 'thread_ts' , " + tmp[x]._source.ts + ")");
      threadLink.setAttribute("href", "#");
      threadLink.setAttribute("style", "position:absolute;right:5%");
      threadLink.textContent = "thread: " + tmp[x]._source.replies;
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



    let attachments = tmp[x]._source.attachments;
    if (attachments && attachments[0].fields) {
      renderInfo(attachments, messageDiv);
    } else if (attachments && attachments[0].service_name) {
      renderServiceMes(attachments, messageDiv);
    } else {
      var message = document.createElement("span");
      message.setAttribute("class", "message_content");
      message.innerHTML = tmp[x]._source.text;
      messageDiv.appendChild(message);
    }

    if (tmp[x]._source.file) {
      var file = document.createElement("img");
      file.setAttribute("src", tmp[x]._source.file.thumb_360);
      var br = document.createElement("br");

      messageDiv.appendChild(br);
      messageDiv.appendChild(file);
    }
    if (loadWhere == "Thread" || loadWhere == "User") {
      document.getElementById("sidebar").appendChild(messageDiv);
    } else if (loadWhere == "mainview") {
      parent = document.getElementsByClassName("message-history")[0].appendChild(messageDiv);
    }
  }
}

function mainview(tmp, loadWhere) {
  parent = document.getElementsByClassName("message-history")[0].innerHTML = null;
  messageDiv = renderMessage(tmp, loadWhere);
}

function sidebar(tmp, loadWhere) {
  document.getElementById("sidebar").innerHTML = null;
  document.getElementById("count").innerHTML = tmp.length;
  document.getElementById("sidebar-with").innerHTML = loadWhere + (name ? name : "");
  messageDiv = renderMessage(tmp, loadWhere);
  w3_open();
}

function loadDoc(url, loadWhere, name = null) {
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function () {
    if (this.readyState == 4 && this.status == 200) {
      var tmp = JSON.parse(decodeHtml(this.responseText)).messages;
      console.log(tmp);

      if (loadWhere == "Thread" || loadWhere == "User") {
        sidebar(tmp, loadWhere);
        document.getElementById("sidebar").appendChild(messageDiv);
      }
      else if (loadWhere == "mainview") {
        mainview(tmp, loadWhere);
      }

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



function logout() {
  let cookie = document.cookie.split("cookie=")[1].split("; ")[0];
  loadDoc("/v1/logout?user=" + cookie, "logout");
  document.cookie = "cookie=;expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
}

function myFunction() {

  let input = document.getElementById("channel-search");
  let filter = input.value.toUpperCase();
  let ul = document.getElementById("channel_list");
  let li = ul.querySelectorAll(".channel_name .user_name");

  for (i = 0; i < li.length; i++) {
    let text = li[i].innerText.toUpperCase();
    if (text.indexOf(filter) > -1) {
      li[i].parentElement.parentElement.style.display = "block";
    } else {
      li[i].parentElement.parentElement.style.display = "none";
    }
  }
}
