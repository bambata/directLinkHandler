==== BUILDING THE DEMO APPLET


1. install maven
2. add maven bin to your PATH env variable

in Terminal Emulator
3. (re)-generate the keystore (usually skipable as keystore already there) : mvn keytool:generateKeyPair
4. mvn clean install
5. Deploy target/DirectLinkHandler-0.1-SNAPSHOT-jar-with-dependencies.jar in your web server and rename it directLink.jar (as in the test.html)
6. to run the example deploy test.htlm, test.js and jquery.js in your web server 
