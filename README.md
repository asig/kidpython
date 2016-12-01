# kidpython

## How to run from command line
`gradle run`

## How to build installer
1. Install NSIS and snapcraft
   `sudo apt install nsis snapcraft`
2. `./gradlew createExe`
3. `(cd src/installer/win; makensis ProgrammableFun.nsi`)
4. `(cd src/installer/deb; dpkg-buildpackage -b)`)

## How to push a release
1. Install python requests package
   `sudo pip install requests`
2. `./scripts/release.py`
