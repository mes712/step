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

/**
 * Adds a random greeting to the page.
 */
function changeCatPhoto() {
  const photos =
      ["/images/cats-1.jpg", "/images/cats-2.jpg", "/images/cats-3.jpg", "/images/cats-4.JPG", "/images/cats-5.jpg"];

  // Pick a random greeting.
  const photo = photos[Math.floor(Math.random() * photos.length)];
  console.log("changed photo to " + photo);

  // Add it to the page.
  const newPhoto = document.getElementById('cat-image');
  newPhoto.src = photo;
}
