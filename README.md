![](images/argon_logo.png)

### Table of contents
- [Description](#description)
- [UI](#user-interface)
  - [Buttons](#buttons)
  - [Clocks and stats](#clocks-and-stats)
  - [Variables](#ui-variables)
- [About clocks](#about-clocks)
  - [What and how](#what-are-clocks)
  - [Example](#example)
  - [Long execution time?](#what-if-a-function-is-slow)
- [Getting started](#getting-started)
  - [Layers](#create-layers)
  - [Variables](#variables)
  - [Clocks](#clocks)
  - [Show](#show-ui)

## Description
### So what is it?
Argon is a powerful Java framework made for code visualization and generative art. It provides intuitive and minimalistic UI, flexible loop management, dynamic interactivity with the code and much more.

### Why would I use it?
If you ever used JavaFX, Swing or AWT you definitely know how much code overhead there is and how tedious it is to write a simple windowed app.  
Argon was built to eliminate all of this and provide lightweight yet versatile framework.
```
Argon argon = new Argon(1000, 1000);
Layer layer = argon.createLayer();

// Your code goes here

argon.show(); 
```
That's it! The code above creates a window, canvas layer and wraps them in slick UI.

## User Interface
![](images/ui.png)  
*"Mountains" example project running on Argon*

### Buttons
![](images/ui_btns.png)
> Reset button sets global framecounter to 0 and calls listeners' `onReset()` method.

> Capture button saves the currently shown rendered image in the runtime directory (the current one) in PNG format.

### Clocks and stats
![](images/ui_stats.png)
<br>
This part of the UI shows user-created clocks. The panel shows the number of calls per clock and how long each call took.  
The last line is always a clocks called "Render". It is created internally and is responsible for rendering an image and displaying it onto the viewport.

### UI Variables
![](images/ui_variables.png)
<br>
Right now there are 3 supported variables: double, integer and boolean (flag). Variables are displayed in the same order as they were added with ` argon.addVariables(...variables) `.

## About clocks
Buttons, variables - that's simple, but exactly is a clock in Argon?
### What are clocks?
Clocks are use-created and Argon-managed loop functions. Each can run at different updaterates (frequencies) from others and Argon keeps track of clocks' relative pace, which ensures parallel-like calls, but in a single thread.  
Clock manager prioritizes completing function calls over realtime rendering. Thus, frequencies can be thought of as target updaterates and become relative if a function takes long time to execute.

### Example
Let's say you want two functions: one to draw something at 60 UPS (updates per second) and another to update something at 90 UPS:
```
argon.addClock("Draw", () -> {
    // Draw smt
}, 60);
argon.addClock("Update", () -> {
    // Update smt
}, 90);
```
Argon ensures that each second first clock will run exactly 60 times and second - 90. The behavior in-between is similar to that of running functions in separate threads (in perfect parallel), except it is executed in one.

### What if a function is slow?
If either of the functions slow down below their maximum time threshold, Argon will reduce every clock proportionally. From the previous example, the first clock's maximum execution time is 16.7ms and second's - 11.1ms. Now let's say first clock's real execution time is 25ms (40 UPS) - a reduction of 33.3%. Therefore, every other clock's frequency is also reduced by 33.3% to preserve relative updaterate. In the UI clocks panel is it will look like this:  
Draw > 40 calls  
Update > 60 calls

## Getting started
### Create instance
First, initiate Argon:
```
Argon argon = new Argon(1920, 1080);
```

### Create layers
There is no one canvas to draw on, but rather drawing is done in layers. Once created, they all have the same dimensions as when creating an Argon instance. Layers are transparent by default and are based on BufferedImage with exposed pixel raster.
```
Layer layer = argon.createLayer();
```
> Layer are stored sequentially, meaning the first created layer will be the top layer.

Layer allows you to access to individual pixels via the provided methods or pixel raster, the backbone `BufferedImage` and `Graphics2D` for more complex shapes and draw operations.
```
layer.image   > BufferedImage
layer.g       > Graphics2D
layer.raster  > Writable integer raster
```

### Variables
Variables are the intermediary between the UI and your code. Currently there are 3 types: `double`, `int` and `boolean`.
```
DoubleVar doubleVar = new DoubleVar("Double variable", 0.3, -1.443, 20.1);
IntVar IntVar = new IntVar("Int variable", 20, 5, 33);
Flag booleanVar = new Flag("Boolean (Flag)", true); 
```
To show them in the UI side panel use `.addVariables(...variables)` method:
```
argon.addVariables(doubleVar, intVar, booleanVar);
```
> The variables are shown in the same order vertically.

### Clocks
To learn about the inner workings of the clocks see [about clocks](#about-clocks) sections.  
To create a clock, [lambda](https://www.w3schools.com/java/java_lambda.asp) expression is preferred.
```
argon.addClock("Clock name", () -> {
    // Your code here
}, 60);
```
Clock's frequency is a target updaterate, which it will try to maintain, but will slow down if the execution time exceeds the expected time.

### Show UI
To finalize and show the window use `show` method.
```
argon.show();
```
> `show` or `showAndStart` must be on the last line in your program. Any changes to Argon after this is might not work properly.

### And more...
For more detailed explanations with examples, see the `examples` folder.
