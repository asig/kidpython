# ProgrammableFun

## How to run from command line
`./gradlew run`

## How to build installer
1. Install NSIS and debhelper
   `sudo apt install nsis debhelper`
2. `./gradlew makePackages`

## How to push a release
1. Install python requests package
   `sudo pip install requests`
2. `./scripts/release.py`
