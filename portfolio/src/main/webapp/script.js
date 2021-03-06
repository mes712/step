// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/** Chooses a random cat photo to display on page. */
function changeCatPhoto() {
  const photos =
      ["/images/cats-1.jpg", 
       "/images/cats-2.jpg",
       "/images/cats-3.jpg", 
       "/images/cats-4.JPG", 
       "/images/cats-5.jpg", 
       "/images/cats-6.jpg", 
       "/images/cats-7.JPG"];

  // Pick a random cat photo.
  const photo = photos[Math.floor(Math.random() * photos.length)];
  
  // Display it on the page.
  const newPhoto = document.getElementById('cat-image');
  newPhoto.src = photo;
}

/* Displays all comments left on page. */
async function getComments(max) {
  document.getElementById('comments-section').innerHTML = '';
  const response = await fetch('/data?max-comments=' + max).then(response => response.json())
  for (const comment of response) {
      document.getElementById('comments-section').innerHTML +=
        comment['commentText'] + ' --' + comment['displayName'] + '(email: ' + comment['email'] + ')<br>';
  }
}

/* Deletes all stored comments. */
async function deleteComments() {
    const response = await fetch('/delete-data', {method: 'POST'});
    getComments(0);
}

/* Fetches blobstore image upload url. */
async function fetchBlobstoreUrl() {
  fetch('/blobstore-url')
      .then((response) => {
        return response.text();
      })
      .then((imageUploadUrl) => {
        const messageForm = document.getElementById('image-form');
        messageForm.action = imageUploadUrl;
      });
}

/* Fetches all image URLs. */
async function getImages() {
  const response = await fetch('/image-form', {method: 'GET'}).then(response => response.json());
  for (const url of response) {
    document.getElementById('image-section').innerHTML += '<img src=' + url + ' height=150>';
  }
}

/* Checks whether user is logged in. Prompts for login if needed, shows comment form otherwise. */
async function checkLogin() {
  const response = await fetch('/verify', {method: 'GET'}).then(response => response.text());
  if (response) {
    document.getElementById('comment-form').classList.remove('hidden'); 
  } else {
    var loginUrl = response;
    document.getElementById('login').innerHTML += 
        '<p>Login <a href=' + loginUrl + '>here</a>.</p>';
  }
}