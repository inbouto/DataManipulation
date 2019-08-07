java -jar dokka-fatjar.jar src -output doc\html
java -jar dokka-fatjar.jar src -output doc -format markdown gfm
cd doc\html
"C:\Program Files\7-Zip\7z.exe" a -tzip C:\Users\Learneo\IdeaProjects\Crypto\doc\html
cd ..
rmdir /Q /S html