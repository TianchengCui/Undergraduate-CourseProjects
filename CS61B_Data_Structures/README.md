# CS 61B — Data Structures

UC Berkeley, Spring 2026. [Course website](https://sp26.datastructur.es/) · [Course policy](https://sp26.datastructur.es/policies/)

| Local folder | Content | Assignment |
| --- | --- | --- |
| [HW01_Setup](HW01_Setup/) | Java setup and arithmetic warm-up | [HW01](https://sp26.datastructur.es/homeworks/hw01/) |
| [HW02_Java_Intro](HW02_Java_Intro/) | Introductory Java programs | [HW02](https://sp26.datastructur.es/homeworks/hw02/) |
| [HW03_IntLists](HW03_IntLists/) | Integer linked lists and recursion | [HW03](https://sp26.datastructur.es/homeworks/hw03/) |
| [HW06_BSTMap](HW06_BSTMap/) | Binary-search-tree map, linked-list map, and timing tests | [HW06](https://sp26.datastructur.es/homeworks/hw06/) |
| [Proj0_Particle_Simulator](Proj0_Particle_Simulator/) | Particle simulator | [Project 0](https://sp26.datastructur.es/projects/proj0/) |
| [Proj1_LinkedListDeque61B](Proj1_LinkedListDeque61B/) | Linked-list deque | [Project 1](https://sp26.datastructur.es/projects/proj1/) |
| [Proj2_ArrayDeque61B](Proj2_ArrayDeque61B/) | Resizable array deque | [Project 2](https://sp26.datastructur.es/projects/proj2/) |
| [Proj3_Percolation](Proj3_Percolation/) | Percolation, visualizers, sample grids, and [project slides](Proj3_Percolation/percolation-slides.pdf) | [Project 3](https://sp26.datastructur.es/projects/proj3/) |
| [Proj4A_NGrams](Proj4A_NGrams/) | N-gram time series and a browser interface | [Project 4A](https://sp26.datastructur.es/proj4/proj4a/) |
| [Proj4CD_Wordnet](Proj4CD_Wordnet/) | WordNet hyponyms, a browser interface, and [design slides](Proj4CD_Wordnet/wordnet-design.pdf) | [4B](https://sp26.datastructur.es/proj4/proj4b/), [4C](https://sp26.datastructur.es/proj4/proj4c/), [4D](https://sp26.datastructur.es/proj4/proj4d/) |
| [Proj5_BYOW](Proj5_BYOW/) | Build Your Own World, a partner project | [5A](https://sp26.datastructur.es/proj5/proj5a/), [5B](https://sp26.datastructur.es/proj5/proj5b/), [5C](https://sp26.datastructur.es/proj5/proj5c/) |

Each assignment keeps its original source layout and available tests. Project 4 contains the full N-gram and WordNet datasets. GitHub stores the two largest CSV copies using Git LFS. Run `git lfs pull` from a clone if you need to fetch them. [Course dataset documentation](https://sp26.datastructur.es/proj4/proj4-data/) Project 5 comes from the partner repository. The local course folders contain no implementation for HW08 or HW09.

HW06 passes the eight base tests. Four extra tests exercise the optional BSTMap methods remove and keySet, which throw UnsupportedOperationException in this copy.

## Run a project

Open an assignment folder in IntelliJ, use a Java 25 SDK, and add the JARs in the shared lib/ folder as dependencies. Set src/ and tests/ as the source and test roots where present. Use the assignment directory as the working directory so relative paths to data and other assets resolve.

With the JDK bin directory on PATH, run these PowerShell commands from the CS 61B directory to compile and launch Project 5.

~~~powershell
Set-Location Proj5_BYOW
$javaFiles = Get-ChildItem -Recurse -File src -Filter '*.java'
javac -encoding UTF-8 -cp '../lib/*' -d out $javaFiles.FullName
java -cp 'out;../lib/*' core.Main
~~~

Projects 4A and 4CD use main.Main as the Java entry point. Open the local address printed by the server. Run them from their respective assignment folders to load the correct data and browser assets.
