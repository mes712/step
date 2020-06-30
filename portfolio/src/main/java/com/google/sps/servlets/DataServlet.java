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

package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

/** Servlet that handles comment data. */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private static final String COMMENT_NAME = "comment";
  private static final String DISPLAY_NAME = "name";
  private static final String DEFAULT_VAL = "";
  private List<Comment> comments = new ArrayList<Comment>();

   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
     response.setContentType("application/json;");
     Gson gson = new Gson();
     String json = gson.toJson(comments);
     response.getWriter().println(json);
   }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String comment = getParameter(request, COMMENT_NAME, DEFAULT_VAL);
    String displayName = getParameter(request, DISPLAY_NAME, DEFAULT_VAL);

    Comment com = new Comment(comment, displayName);
    comments.add(com);
    response.sendRedirect("/index.html");
  }

    /**
   * Returns parameter of given name from servlet request, or a default value if not specified.
   *
   * @param request -- the servlet request from client
   * @param name -- the name of the request parameter to get
   * @param defaultValue -- default value to return if not specified
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client.
   */
  private static String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null || value.equals("")) {
      return defaultValue;
    }
    return value;
  }

  /* Holds all data for a single comment. */
  private class Comment {
    private String commentText;
    private String displayName;

    public Comment(String commentText, String displayName) {
      this.commentText = commentText;
      this.displayName = displayName;
    }
  }
}  
