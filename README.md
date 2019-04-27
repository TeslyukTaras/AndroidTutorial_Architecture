# Android Architecture Tutorial
This sample project shows use of different architecture patterns.<p>
This is a weather application which displays information about:<p>
- current weather<p>
- 24 hour weather<p>
- 5 day weather<p>
Application load information from server and store information locally.<p>
User is able to change the city.<p>
  <p align="center">
  <img src="doc/app_Screenshot.png" width="250" title="application screenshot">
  <p>
 At the begining, all logic is stored in MainActivity class.<p>

## General project transformation 
Project improvements were divided into several steps:<p>
- Step 1: Use MVC architecture to seperate business and presentation logic.<p>
- Step 2: Use MVP to hide View from Presenter and increase testing capabilities.<p>
- Step 3: Use Repository and Data Sources to manage local database and get data from remote server.<p>
- Step 4: Implement Use Cases to move business logic out from Presenter.<p>
- Step 5: Migrate to MVVM to support reactive components.<p>
  
## Step 1: Use MVC architecture to seperate business and presentation logic.<p>
Source code is present at: https://github.com/TeslyukTaras/AndroidTutorial_Architecture/tree/mvc
General Class diagram:<p>
  <p align="center">
  <img src="doc/MVC.png" title="MVC">
  <p>
    
## Step 2: Use MVP to hide View from Presenter and increase testing capabilities.<p>
Source code is present at: https://github.com/TeslyukTaras/AndroidTutorial_Architecture/tree/mvp  
General Class diagram:<p>
  <p align="center">
  <img src="doc/MVP.png" title="MVP">
  <p>
    
## Step 3: Use Repository and Data Sources to manage local database and get data from remote server.<p>
Source code is present at: https://github.com/TeslyukTaras/AndroidTutorial_Architecture/tree/mvp_repository_datasource
Key point is to divide Model into several parts:<p>
- Repository, which manage Data Sources and take decisions which one to use.<p>
- Local data source, which work with local DB.<p>
- Remote data source, which work with remote server.<p>
  <p align="center">
  <img src="doc/Model_transformation.png" title="Model transformation">
  <p>
General Class diagram will transform to:<p>
  <p align="center">
  <img src="doc/MVP_Repository.png" title="MVP with repository">
  <p>
    
## Step 4: Implement Use Cases to move business logic out from Presenter.<p>
Source code is present at: https://github.com/TeslyukTaras/AndroidTutorial_Architecture/tree/mvp_clean_architecture
Key point is to move business logic to separate classes:<p>
- Use Case to load weather data.<p>
- Use Case to find current weather.<p>
- Use Case to filter 24hour weather.<p>
- Use Case to find 5 day weather.<p>
  <p align="center">
  <img src="doc/UseCase_transformation.png" title="UseCase transformation">
  <p>
General Class diagram will transform to:<p>
  <p align="center">
  <img src="doc/MVP_Clear.png" title="MVP Clean Like">
  <p>
    
## Step 5: Migrate to MVVM to support reactive components.<p>
Source code is present at: https://github.com/TeslyukTaras/AndroidTutorial_Architecture/tree/mvvm
  <p align="center">
  <img src="doc/MVVM.png" title="MVVM">
  <p>
What is next:<p>
- Save information about selected Cities.<p>
- Improve MVVM sample to use DataBinding from JetPack.<p>
- Integrate Dagger2.<p>

## Results
Let's compare different architectures and extensions by the next criterias:<p>
- Class count<p>
  <p align="center">
  <img src="doc/compare_class.png" title="Compare by classes count">
  <p>
- Lines of code<p>
  <p align="center">
  <img src="doc/compare_lines.png" title="Compare by lines count">
  <p>


