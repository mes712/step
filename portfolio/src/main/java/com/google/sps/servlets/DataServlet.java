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
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.common.collect.ImmutableList;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.common.collect.Streams;
import static com.google.common.collect.ImmutableList.toImmutableList;

/** Servlet that handles comment data. */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private static final String COMMENT_NAME = "comment";
  private static final String DISPLAY_NAME = "name";
  private static final String DEFAULT_VAL = "";

   @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json;");
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    ImmutableList<Comment> comments = Streams.stream(results.asIterable())
        .map(entity -> (new Comment((String) entity.getProperty("comment"), (String) entity.getProperty("name"))))
        .collect(toImmutableList());

    Gson gson = new Gson();
    String json = gson.toJson(comments);
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String commentText = getParameter(request, COMMENT_NAME, DEFAULT_VAL);
    String displayName = getParameter(request, DISPLAY_NAME, DEFAULT_VAL);
    long timestamp = System.currentTimeMillis();

    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("comment", commentText);
    commentEntity.setProperty("name", displayName);
    commentEntity.setProperty("timestamp", timestamp);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);
    response.sendRedirect("/index.html");
  }

    /**
   * Returns value of request parameter name, or a default value if not specified.
   * @param request -- client comment servlet request
   * @param name -- parameter of servlet request to return
   * @param defaultValue -- value to return if not specified by client
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