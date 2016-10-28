# Version History
All API breaking features, minor changes, and bug fixes will be documented here.

The format is based on [Keep a Changelog](http://keepachangelog.com/) 
and this project adheres to [Semantic Versioning](http://semver.org/).

## [0.0.0] - 2016-10-13
- Created repository and started new Android project

## [0.1.0] - 2016-10-18
- Hooked app up to Firebase database
- Added new ShoppingList activity for manipulating the shopping list
- App can now display shopping list in real time
- App can add new shopping items in real time

## [0.2.0] - 2016-10-19
- User can now remove items from the list by long-pressing one and selecting 'remove'
- User can now remove all items by long-pressing one and selecting 'remove all'
- Items' `crossed` state now persists and displays appropriately, even on startup or resume

## [0.2.1] - 2016-10-20
- Fixed problem where user could add a blank item to the database

## [0.2.2] - 2016-10-20
- Fixed problem where user could delete an item after another user selects it

## [0.3.2] - 2016-10-20
- The items' incrementing ID now resets after remove-all is performed

## [0.4.2] - 2016-10-20
- Limited item length to 35 characters

## [0.5.2] - 2016-10-24
- Added blank test layout to new activity

## [0.6.2] - 2016-10-25
- Added basic authentication
- User must now log in with authorized account to read/write to database
- Added action bar overflow menu for navigating through the app and signing out
- Refactored shared code into new BaseActivity for reuse

## [0.7.2] - 2016-10-26
- Added `prod` and `develop` product flavors to build file
- App now has two separate databases, one for production and one for development

## [0.8.2] - 2016-10-28
- Login screen now features progress circle to indicate loading
- Shopping list screen also features progress circle
- Shopping list database fetch logic moved to its own worker thread
