# Zerops x Scala

A bare-bones example of a Scala backend using the Play Framework. It provides a lightweight starting point for building web applications with minimal configuration. Zerops makes deploying and running Ember.js apps, a breeze.

![Scala](https://github.com/zeropsio/recipe-shared-assets/blob/main/covers/svg/cover-scala.svg)

## Recipe features

- Latest version of **Scala** & **JDK** running on **Zerops Ubuntu** Service.
- Latest version of **SBT(Simple build tool)** with **Play** web framework.

## Server backend

By default, the project uses the Pekko HTTP Server backend. To switch to the Netty Server backend, enable the `PlayNettyServer` sbt plugin in the `build.sbt` file.
In the `build.sbt` of this project, you'll find a commented line for this setting; simply uncomment it to make the switch.
For more detailed information, refer to the Play Framework [documentation](https://www.playframework.com/documentation/3.0.x/Server).


<br/>

## Production vs. development

This recipe is ready for production as is, and will scale horizontally by adding more containers in case of high traffic surges. If you want to achieve the highest baseline reliability and resiliace, start with at least two containers (add `minContainers: 2` in recipe YAML in the `app` service section, or change the minimum containers in "Automatic Scaling configuration" section of service detail).

<br/>

## Changes made over the default installation

If you want to modify your existing Scala backend to efficiently run on Zerops, there are no changes needed in the codebase on top of the standard installation, just add [zerops.yml](https://github.com/zeropsio/recipe-scala/blob/main/zerops.yml) to your repository.

<br/>

Need help setting your project up? Join [Zerops Discord community](https://discord.com/invite/WDvCZ54).
