
var TmplManager = function () {
  var templates = {};

  return {

    load: function (pathes, callback) {
      var loadTemplate = function (counter) {
        var path = pathes[counter];
        $.get(path, function (data){
          if (counter < pathes.length) {
            templates[path] = data;
            ++counter;
            loadTemplate(counter);
          } else {
            callback();
          };
        }); 
      };
      loadTemplate(0);
    },

    get: function (path) {
      return templates[path];
    },

    clear: function () {
      templates = {};
    }
  };
}();
