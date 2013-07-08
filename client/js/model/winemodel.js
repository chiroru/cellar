window.Wine = Backbone.Model.extend({
    urlRoot:"http://127.0.0.1:8080/cellar/rest/wines/",
    defaults:{
        "id":null,
        "name":"",
        "grapes":"",
        "country":"USA",
        "region":"California",
        "year":"",
        "description":"",
        "picture":""
    }
});

window.WineCollection = Backbone.Collection.extend({
    model:Wine,
    url:"http://127.0.0.1:8080/cellar/rest/wines"
});
