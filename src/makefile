# makefile for building

SOURCES = $(wildcard */*.java)
CLASSES = $(SOURCES:.java=.class) 

default: $(CLASSES)

%.class: %.java
	javac -cp . $<

clean:
	rm -f */*.class