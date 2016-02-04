
##Structure
All template files are loaded as a [WebJars](http://www.webjars.org/) dependency to Sunrise. This includes [Handlebars](http://handlebarsjs.com/) source templates, all web assets (i.e. CSS, JS, images and fonts) and i18n [YAML](http://www.yaml.org/) files. The expected structure being as follows:

```
META-INF
+-- resources
    +-- webjars
        +-- css
        +-- fonts
        +-- img
        +-- js
        +-- i18n
        |   +-- de
        |   +-- en
        |   +-- ..
        +-- templates
```

###Template
Sunrise uses [Handlebars.java](https://jknack.github.io/handlebars.java/) by default as a template engine.

In order to find the corresponding template source file, it searches first inside the classpath `/templates`. If the file is not found there, then it tries inside the Template's Webjars dependency (i.e. `/META-INF/resources/webjars/templates`). This enables a practical way to override parts of the template without the need of replacing it completely, as we will see in the section _[Customize HTML](#customize-html)_.

Learn how to modify this behaviour in _[Change template source loaders](#change-template-source-loaders)_.

###Web Assets
There are two types of routes that serve web assets in Sunrise:
- `/assets/{css|js|fonts|img}/`: Serves files inside the `css`, `js`, `fonts` or `img` folder of the Template's Webjars. This route allows to access the web assets provided by the template.
- `/assets/public/`: Serves files from the project's `public` folder. By placing web assets in this folder, you can easily extend Sunrise's functionality, as explained in _[Customize Web Assets](#customize-web-assets)_.

###Internationalization
Sunrise uses [YAML](http://www.yaml.org/) files by default to provide text in different languages. Translations are grouped according to the page or section they belong, which is known as bundles (e.g. `home`, `checkout`). Each YAML file contains the translated text for a particular language and bundle.

The following structure would be used to have translations in German and English for the home and checkout bundles:

```
locales
+-- de
|   +-- home.yaml
|   +-- checkout.yaml
+-- en
    +-- home.yaml
    +-- checkout.yaml
```

Similarly as it works with templates, the application tries to find the translated text first inside the classpath `/locales`. If that particular translation is not found there, then it tries inside the Template's Webjars dependency (i.e. `/META-INF/resources/webjars/locales`). This enables a practical way to override a particular translation without the need of replacing them all, as it is explained in the section _[Customize Internationalization](#customize-internationalization)_.

Learn how to modify this behaviour in _[Change i18n resource loaders](#change-i18n-resource-loaders)_.


##Basic Customization

####Customize HTML

This guide shows you how to modify the templates in an easy and convenient way. Note it is assuming the configuration has not been changed.

Run Sunrise and open it in a browser to inspect the part of the code you want to change. In the website's source code you will find comments indicating the template source file containing each component. In the following example we can see that the template source file containing the logo is `common/logo.hbs`.

```html
...
<div class="col-sm-8">
  <!-- start common/logo.hbs -->
  <a href="/en/home" class="brand-logo">
    <img class="img-responsive" src="/assets/img/logo.svg" alt="SUNRISE">
  </a>
  <!-- end common/logo.hbs -->
</div>
...
```

In order to override this component we need to create a Handlebars template source file in `conf/templates` with the desired content, so that the new code is used instead of the original code. To do so, Sunrise offers a convenient [SBT](http://www.scala-sbt.org/) command `copyTemplateFiles`, which given a whitespace-separated list of template files copies them from the original location to `conf/templates`.

Let's execute it for the previous example:

```shell
sbt 'copyTemplateFiles common/logo.hbs'
```

After the execution, `conf/templates/common/logo.hbs` has been created with the content of the original template source file:

```hbs
<!-- start common/logo.hbs -->
<a href="{{@root.meta._links.home.href}}" class="brand-logo">
  <img class="img-responsive" src="{{@root.meta.assetsPath}}img/logo.svg" alt="SUNRISE">
</a>
<!-- end common/logo.hbs -->
```

With that, you are ready to start modifying the template. For example, let's replace the logo image with a simple text:

```hbs
<!-- start common/logo.hbs -->
<a href="{{@root.meta._links.home.href}}" class="brand-logo">
  Sunrise
</a>
<!-- end common/logo.hbs -->
```

If you run Sunrise and reload the page, the image has been effectively replaced by the text:

```html
...
<div class="col-sm-8">
  <!-- start common/logo.hbs -->
  <a href="/en/home" class="brand-logo">
    Sunrise
  </a>
  <!-- end common/logo.hbs -->
</div>
...
```

To learn how to write Handlebars templates, please check the [Handlebars.js](http://handlebarsjs.com/) documentation. In particular, the sections about [Expressions](http://handlebarsjs.com/expressions.html), [Built-In Helpers](http://handlebarsjs.com/builtin_helpers.html) and [@data Variables](http://handlebarsjs.com/reference.html#data).


####Customize Web Assets

#####Customize images
If you want to provide your own images, you just have to place them inside the `public/images/` folder and modify the HTML accordingly as explained in the section _[Customize HTML](#customize-html)_. Following the logo example used in that section, you should obtain:

```hbs
<!-- start common/logo.hbs -->
<a href="{{@root.meta._links.home.href}}" class="brand-logo">
    <img class="img-responsive" src="{{@root.meta.assetsPath}}public/images/yourlogo.png" alt="YOUR SITE">
</a>
<!-- end common/logo.hbs -->
```

Notice that the image path has been replaced to `public/images/`.

#####Customize CSS
Sunrise comes with the file `public/stylesheets/sunrise.css` where you can add your own CSS rules. As this is the last CSS file loaded of the website, from here you can override any previous rule set by the template.

If you want to provide your own CSS file instead, you just have to place the file inside the `public/stylesheets/` folder and add the HTML `<link>` tag in the template source file `conf/templates/common/additional-html-head.hbs`, as shown in the example:

```hbs
<link rel="stylesheet" href="/assets/public/stylesheets/sunrise.css"/> <!-- default sunrise CSS file -->
<link rel="stylesheet" href="/assets/public/stylesheets/yourfile.css"/> <!-- your CSS file -->
```

#####Customize JavaScript
Sunrise comes with the file `public/javascripts/sunrise.js` where you can add your own JavaScript code.

If you want to provide your own JavaScript file instead, you just have to place the file inside the `public/javascripts/` folder and add the HTML `<script>` tag in the template source file `conf/templates/common/additional-html-scripts.hbs`, as shown in the example:

```hbs
<script src="/assets/public/javascripts/sunrise.js"></script> <!-- default sunrise JS file -->
<script src="/assets/public/javascripts/yourfile.js"></script> <!-- your JS file -->
```

####Customize `<head>`
You may need to provide additional HTML `<meta>` tags or other kind of information to the HTML `<head>`. To do so, just add them to the template source file `conf/templates/common/additional-html-head.hbs`, as shown in the example:

```hbs
<link rel="stylesheet" href="/assets/public/stylesheets/sunrise.css"/> <!-- default sunrise CSS file -->
<meta name="description" content="My description"> <!-- your meta tag -->
```


##Advanced Customization

####Change template source loaders
If you want to change the way templates sources are loaded, you can change the list of template loaders you want to use with `handlebars.templateLoaders`.

Along with the `path`, you can also specify the `type` of path you are providing, which can be either `classpath` or `file`. Keep in mind that the list order determines the order in which the loaders are going to be invoked.

```hocon
handlebars.templateLoaders = [
  {"type":"classpath", "path":"/path/to/files/on/classpath"}, # tries first
  {"type":"file", "path":"relative/path/to/files"}, # tries second, if first failed
  {"type":"file", "path":"/absolute/path/to/files"} # tries third, if all other failed
]
```

##Miscellaneous

Some related literature to fully understand Sunrise:

######Play Framework
* https://www.playframework.com/documentation/2.4.x/Home
* book http://www.manning.com/leroux/

######Java 8
* book Java SE 8 for the Really Impatient http://www.horstmann.com/java8/index.html

######SBT (Build Tool)
* http://www.scala-sbt.org/0.13/tutorial/index.html
