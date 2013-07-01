// Models
window.Wine = Backbone.Model.extend();

window.WineCollection = Backbone.Collection.extend({
    model:Wine,
    url:"http://localhost:8080/cellar/rest/wines"
});


// Views
window.WineListView = Backbone.View.extend({

    tagName:'ul',

    initialize:function () {
        this.model.bind("reset", this.render, this);
    },

    render:function (eventName) {
        _.each(this.model.models, function (wine) {
            $(this.el).append(new WineListItemView({model:wine}).render().el);
        }, this);
        return this;
    }

});

window.WineListItemView = Backbone.View.extend({

    tagName:"li",

    template:_.template($('#tpl-wine-list-item').html()),

    render:function (eventName) {
        $(this.el).html(this.template(this.model.toJSON()));
        return this;
    }

});

window.WineView = Backbone.View.extend({

    template:_.template($('#tpl-wine-details').html()),
    default:_.template($('#tpl-wine-details-default').html()),

    render:function (eventName) {
        $(this.el).html(this.template(this.model.toJSON()));
        return this;
    },

    reset:function() {
        $(this.el).html(this.default());
        return this;
    }
});

// Router
var AppRouter = Backbone.Router.extend({
    self: this,
    routes:{
        "":"list",
        "wines/:id":"wineDetails"
    },

    list:function () {
        self.wineList = new WineCollection();
        self.wineListView = new WineListView({model:wineList});
        wineList.fetch({
          success: function() {
            $('#sidebar').html(self.wineListView.render().el);
          }
        });
        self.wineView = new WineView();
        $('#content').html(self.wineView.reset().el);
        // $('#sidebar').html(this.wineListView.render().el);
    },

    wineDetails:function (id) {
        self.wine = self.wineList.get(id);
        self.wineView = new WineView({model:self.wine});
        $('#content').html(self.wineView.render().el);
    }
});

var app = new AppRouter();
Backbone.history.start();
