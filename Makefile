JFLAG1 = -cp 
JFLAG2 = -d 
JC = javac
.SUFFIXES: .java .class
.java.class:
        $(JC) $(JFLAG1) ./src/ $(JFLAG2) ./out/ $*.java

CLASSES = \
        ./src/AbstractBTree.java \
        ./src/AbstractBTreeNode.java \
        ./src/BTree.java \
        ./src/BTreeNode.java \
        ./src/FileContainer.java \
        ./src/FSC.java \
        ./src/OverflowNode.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
        $(RM) ./out/*.class