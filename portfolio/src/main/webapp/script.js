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
       "/images/cats-7.jpg"];

  // Pick a random cat photo.
  const photo = photos[Math.floor(Math.random() * photos.length)];
  
  // Display it on the page.
  const newPhoto = document.getElementById('cat-image');
  newPhoto.src = photo;
}
