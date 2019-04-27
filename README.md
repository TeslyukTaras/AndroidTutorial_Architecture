# AndroidTutorial_Architecture
Sample show use of different architecture patterns.<p>
Preconditions: There is a simple Weather application. It display information about current weather, 24hour and 5 days upfront. At this point, all logic stored in MainActivity class.<p>
General project transformation were divided into several steps:<p>
Step 1: Use MVC architecture to seperate business and presentation logic.<p>
Step 2: Use MVP to hide View from Presenter and increate testing capabilities.<p>
Step 3: Use Repository and Data Sources to manage local database and get data from remote server.<p>
Step 4: Implement Use Cases to move business logic out from Presenter.<p>
Step 5: Migrate to MVVM to support reactive components.<p>

## MVC transformation is present at:
https://github.com/TeslyukTaras/AndroidTutorial_Architecture/tree/mvc

## MVP transformation is present at:
https://github.com/TeslyukTaras/AndroidTutorial_Architecture/tree/mvp

## MVP + Repository + Data Source transformation is present at:
https://github.com/TeslyukTaras/AndroidTutorial_Architecture/tree/mvp_repository_datasource

## MVP + Use Cases transformation is present at:
https://github.com/TeslyukTaras/AndroidTutorial_Architecture/tree/mvp_clean_architecture

## MVVM transformation is present at:
https://github.com/TeslyukTaras/AndroidTutorial_Architecture/tree/mvvm
