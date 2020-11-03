# Marvel OpenBank Android Test

[![N|Solid](https://upload.wikimedia.org/wikipedia/commons/thumb/0/04/MarvelLogo.svg/800px-MarvelLogo.svg.png)](https://developer.marvel.com/)

Android app that shows a list of Marvel comic characters obtained from the web https://developer.marvel.com/.

# Functioning

Upon accessing the app, it will show us a loading presentation with the Marvel company logo and after a few seconds we will access a list with a heading named "Characters Marvel".

#### Characters Marvel screen

In the header we also have an icon with a magnifying glass that allows us to discover a text bar to make a search within the list.
The list shows a round image for each character, their name on the right and an arrow pointing to the right that indicates more information.
The search is by content and is activated dynamically, so as we write we can see the results live.
Another feature of this first screen is that as we go down the list we will load more items that will be stored for the next time we open the app.

#### Character Detail screen

On this screen we have a header with the title "Character Detail" and an arrow to return to the previous screen. In the rest of the screen we will see the name of the character above an image that will load if it is not already, under said image the last time that character appeared in a comic and a description of it below.

## Technological functionalities

The app is developed in Kotlin language, using the MVVM pattern.

##### The project is structured as follows:
//////////////////////////////////////////
---> Data Folder (It contains everything related to the data and its obtaining)
--------> Entities Folder (Model for db and network)
--------> Local Folder (Tools for configure and use db)
-------> Remote Folder (Tools for calls to network)
-------> Repository Folder (Bridge between local and remote use)
---> Di Folder (It contains the configuration for dependency injection)
---> Ui Folder (It contains the activity main, fragments, adaptors and viewmodels)
-------> CharacterDetail Folder (For screen Character detail)
-------> Character Folder (For screen Characters Marvel)
---> Utils Folder (It contains generic tools for app)


The classes that make up the views section are:

* [CharactersFragment.kt] - Class that represents the Fragment that contains the list of Marvel characters.
* [CharacterDetailFragment.kt] - Class that represents the Fragment that contains the details of the Marvel character.


