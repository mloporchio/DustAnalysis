#
#	File:		makefile
#	Author:		Matteo Loporchio
#	Thanks to:	https://www.cs.swarthmore.edu/~newhall/unixhelp/javamakefiles.html
#

JC=javac
JFLAGS=-cp ".:./lib/guava-31.1-jre.jar"

default: 
	$(JC) $(JFLAGS) *.java

clean:
	$(RM) *.class