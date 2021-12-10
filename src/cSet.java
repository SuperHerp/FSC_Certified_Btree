// kls::comment:: description of this class (keyquestions: Why this class, what does it generally do? What is expected from the user? What does the user get back?)
// kls::comment:: some use cases for problems, that can be solved using this class.
public class cSet extends Object {
    private int x; 	// kls::comment:: input: e.g.: the width  of the rectangular area
    private int y;	// kls::comment:: e.g.: input: the height --------;;------------- 
    private int threads;	// kls::comment:: input: the number of worker-threads that shall process the rectangular area
    
    private int sections;	// kls::comment:: ?? seems to be output

    private int xTLen;	// kls::comment:: output: the width  of the working-unit 
    private int yTLen;	// kls::comment:: output: the height ----------;;---------

    private int xRest;	// kls::comment:: 
    private int yRest;	// kls::comment:: 
    private boolean xR;	// kls::comment:: 
    private boolean yR;	// kls::comment:: 
    private boolean xyR;// kls::comment:: 

    private int xStep;	// kls::comment:: 
    private int yStep;	// kls::comment:: 

    private int [] blkChain;	// kls::comment:: imho it shall be described more detailed (maybe using links(meaned only logically) to the general description at class begin.)
    private int [][][] blkMap;	// kls::comment:: -------------------------------;;-------------------------------------

    int checkPoint = 0;	// kls::comment:: 

    public cSet(int x, int threads){
        this.x = x;
        this.y = 1;
        this.threads = threads;
        this.sections= threads*4;

        /*Calculation max. section length*/
        if (sections > this.x){                   // is only bigger x
            sections = this.x;                      //x is minimum
        }

        /*Calculate x-width of block*/
        xStep = (this.x/sections);

        /*In case sections is greater than x => set sections to x*/
        if (xStep == 0){
            xStep = 1; 
            sections = this.x;
        }

        /*Calculate x-length of actual field*/
        xTLen = xStep*sections;


        // /*Calculate y-width of block*/
        // yStep = (this.y/sections);
        yStep = 1;

        // /*In case sections is greater than y => set sections to y*/
        // if (yStep == 0){
        //     yStep = 1;
        //     sections = this.y;
        // }

        // /*Calculate y-length of actual field*/
        // yTLen = yStep*sections;

        /*Calculate x-Rest*/
        xRest = this.x-xTLen;
        if (xRest != 0){
            xR = true;
        }

        // /*Calculate y-Rest*/
        // yRest = this.y-yTLen;
        // if (yRest != 0){
        //     yR = true;
        // }
        yRest = 0;
        yR = false;

        // /*if both x and y have rest -> set xyR => true*/
        // if (xR == true && yR == true){
        //     xyR = true;
        // }

        /*Create array with length of number of blocks squared, contains only zeros*/
        blkChain = new int[sections*this.y];

        /*Create array containing start and end coordinates for every individual block*/
        blkMap = new int [sections][this.y][4];

        /*fill array with actual start and end coordinates*/
        for (int xAxis = 0; xAxis < sections; xAxis++) {
            for (int yAxis = 0; yAxis < this.y; yAxis++) {
                int xOverlap = 0;
                int yOverlap = 0;
                if (xAxis == blkMap.length-1){                  //has x-rest
                    if (yAxis == blkMap[0].length-1){           //has x and y-rest
                        xOverlap = xRest;                           //set xOverlap to xRest for current block
                        yOverlap = yRest;                           //set yOverlap to yRest for current block
                    }else{                                      //only has x-rest
                        xOverlap = xRest;                           //set xOverlap to xRest for current block
                    }
                }else if (yAxis == blkMap[0].length-1){         //only has y-rest
                    yOverlap = yRest;                               //set xOverlap to xRest for current block
                }
                blkMap[xAxis][yAxis][0] = xStep*xAxis;                      //set x-start-coord for current block
                blkMap[xAxis][yAxis][1] = yStep*yAxis;                      //set y-start-coord for current block
                blkMap[xAxis][yAxis][2] = (blkMap[xAxis][yAxis][0]+xStep+xOverlap);   //set x-end-coord for current block (with additional xOverlap)
                blkMap[xAxis][yAxis][3] = (blkMap[xAxis][yAxis][1]+yStep+yOverlap);   //set y-end-coord for current block (with additional yOverlap)
            }
        }
    }

    public cSet(int x, int y, int threads){
        this.x = x;
        this.y = y;
        this.threads = threads;
        this.sections= threads;

        /*Calculation max. section length*/
        if (sections > this.x){                 // is bigger x
            if (sections > this.y){                 // is bigger than y and x
                if ((this.x-this.y) < 0){               // find minimum(x, y)
                    sections = this.x;                      // x is minimum
                }else {
                    sections = this.y;                      // y is minimum
                }
            }else {                                 // is only bigger x
                sections = this.x;                      //x is minimum
            }
        }else if (sections > this.y){           // is only bigger y
            sections = this.y;                      //y is minimum
        }

        /*Calculate x-width of block*/
        xStep = (this.x/sections);

        /*In case sections is greater than x => set sections to x*/
        if (xStep == 0){
            xStep = 1; 
            sections = this.x;
        }

        /*Calculate x-length of actual field*/
        xTLen = xStep*sections;


        /*Calculate x-width of block*/
        yStep = (this.y/sections);

        /*In case sections is greater than x => set sections to x*/
        if (yStep == 0){
            yStep = 1;
            sections = this.y;
        }

        /*Calculate x-length of actual field*/
        yTLen = yStep*sections;

        /*Calculate x-Rest*/
        xRest = this.x-xTLen;
        if (xRest != 0){
            xR = true;
        }

        /*Calculate y-Rest*/
        yRest = this.y-yTLen;
        if (yRest != 0){
            yR = true;
        }
        if (xR == true && yR == true){
            xyR = true;
        }

        /*Create array with length of number of blocks squared, contains only zeros*/
        blkChain = new int[sections*sections];

        /*Create array containing start and end coordinates for every individual block*/
        blkMap = new int [sections][sections][4];

        /*fill array with actual start and end coordinates*/
        for (int xAxis = 0; xAxis < sections; xAxis++) {
            for (int yAxis = 0; yAxis < sections; yAxis++) {
                int xOverlap = 0;
                int yOverlap = 0;
                if (xAxis == blkMap.length-1){                  //has x-rest
                    if (yAxis == blkMap[0].length-1){           //has x and y-rest
                        xOverlap = xRest;                           //set xOverlap to xRest for current block
                        yOverlap = yRest;                           //set yOverlap to yRest for current block
                    }else{                                      //only has x-rest
                        xOverlap = xRest;                           //set xOverlap to xRest for current block
                    }
                }else if (yAxis == blkMap[0].length-1){         //only has y-rest
                    yOverlap = yRest;                               //set xOverlap to xRest for current block
                }
                blkMap[xAxis][yAxis][0] = xStep*xAxis;                      //set x-start-coord for current block
                blkMap[xAxis][yAxis][1] = yStep*yAxis;                      //set y-start-coord for current block
                blkMap[xAxis][yAxis][2] = ((xAxis*xStep)+xStep+xOverlap);   //set x-end-coord for current block (with additional xOverlap)
                blkMap[xAxis][yAxis][3] = ((yAxis*yStep)+yStep+yOverlap);   //set y-end-coord for current block (with additional yOverlap)
            }
        }
    }

	// kls::comment:: simple interface setter and getter methods at the end. More complex methods/functions at the beginning.
    /** [getX()]
     *
     * returns x-width(int) of specified cSet
     */
    public int getX(){
        return this.x;
    }

    /** [getY()]
     *
     * returns y-width(int) of specified cSet
     */
    public int getY(){
        return this.y;
    }

    /** [getSec()]
     *
     * returns number(int) of sections in x-Direction(y-Direction) of specified cSet
     */
    public int getSec(){
        return this.sections;
    }

    /** [getXR()]
     *
     * returns rest(int) in x-Direction [rest after dividing field] of specified cSet
     */
    public int getXR(){
        return this.xRest;
    }

    /** [getYR()]
     *
     * returns rest(int) in y-Direction [rest after dividing field] of specified cSet
     */
    public int getYR(){
        return this.yRest;
    }

    /** [getXStep()]
     *
     * returns blockwidth(int) in x-Direction of specified cSet
     */
    public int getXStep(){
        return this.xStep;
    }

    /** [getYStep()]
     *
     * returns blockwidth(int) in y-Direction of specified cSet
     */
    public int getYStep(){
        return this.xStep;
    }

    /** [getBlkCount()]
     *
     * returns number(int) of blocks inside the blkMap/blkChain/cSet
     */
    public int getBlkCount(){
        return this.blkChain.length;
    }

    /** [hasXR()]
     *
     * returns true(boolean) if x-rest exists (false otherwise) in specified cSet
     */
    public boolean hasXR(){
        return this.xR;
    }

    /** [hasYR()]
     *
     * returns true(boolean) if y-rest exists (false otherwise) in specified cSet
     */
    public boolean hasYR(){
        return this.yR;
    }

    /** [hasXYR()]
     *
     * returns true(boolean) if x-rest AND y-rest exist (false otherwise) in specified cSet
     */
    public boolean hasXYR(){
        return this.xyR;
    }
    /** [getBlkChain()]
     *
     * returns array(int []) with length of sections^2
     * array is filled with zero
     *
     * array[i] represents current work state for block-Nr: i
     *      0 = no work has been done
     *      1 = work has started/ work is done
     */
    public int[] getBlkChain(){
        return this.blkChain;
    }

    /** [getBlkMap()]
     *
     * returns array(int [][][]) containing every block of specified cSet
     *
     * array[sections][sections][4]
     * 1st Dimension: Move along x-Axis inside array
     * 2nd Dimension: Move along y-Axis inside array
     * 3rd Dimension: [0] x-start-coordinate for block at array[x-Axis][y-Axis]
     *                [1] y-start-coordinate for block at array[x-Axis][y-Axis]
     *                [2] x-end-coordinate   for block at array[x-Axis][y-Axis]
     *                [3] y-end-coordinate   for block at array[x-Axis][y-Axis]
     */
    public int[][][] getBlkMap(){
        return this.blkMap;
    }

    /** [getBlkXYPos(blkNR)]
     *
     * @param blkNr(int) := number corresponding to unique block inside blkChain/blkMap/cSet
     *
     * kls::comment:: a litle bit more detailed description (maybe with logical links to the general description)
     * returns array(int []) containting x and y coordinates(inside blkMap) of specified block
     *
     * array[2]
     * 1st Dimension: array[0] contains x-coordinate
     *                array[1] contains y-coordinate
     */
    public int[] getBlkXYPos(int blkNr){
        int xAxis= -1;
        int yAxis= -1;
        boolean srchBlkNr = true;
        while (srchBlkNr) {

            for (int xtmp = 0; xtmp < this.sections; xtmp++) {
                if(this.y == 1){
                    if(xtmp < blkNr){
                        continue;
                    }else{
                        xAxis = xtmp;
                        yAxis = 0;
                        srchBlkNr = false;
                        break;
                    }
                }
                for (int ytmp = 0; ytmp < this.sections; ytmp++) {
                    if (blkNr == (xtmp * this.sections) + ytmp) {
                        xAxis = xtmp;
                        yAxis = ytmp;
                        srchBlkNr = false;
                        break;
                    } else {
                        continue;
                    }
                }
                if (srchBlkNr == false) {
                    break;
                }
            }
        }
        int [] xyCoord = {xAxis, yAxis};
        return xyCoord;
    }

    /** [getBlkXYStEn2(xAxis, yAxis)]
     *
     * @param xAxis(int) := number corresponding to x-coordinate inside blkMap
     * @param yAxis(int) := number corresponding to y-coordinate inside blkMap
     *
     * kls::comment:: a litle bit more detailed description (maybe with logical links to the general description)
	 * kls::comment:: "StEn" What is the "expansion" of this acronim? Link to general description?
     * returns array(int []) containting x-start, x-end, y-start and y-end of block at blkMap[xAxis][yAxis][i]
     *
     * array[4]
     * 1st Dimension: array[0] contains x-start-coordinate
     *                array[1] contains y-start-coordinate
     *                array[2] contains x-end-coordinate
     *                array[3] contains y-end-coordinate
     */
    public int[] getBlkXYStEn2(int xAxis, int yAxis){
        int [] xyStEn = {this.blkMap[xAxis][yAxis][0], this.blkMap[xAxis][yAxis][1], this.blkMap[xAxis][yAxis][2], this.blkMap[xAxis][yAxis][3]};
        return xyStEn;
    }

    /** [getBlkXYStEn(blkNR)]
     *
     * @param blkNr(int)
     *
     * kls::comment:: StEn and StEn2(before), suggestion: more detailed description
     * returns array(int []) containting x-start, x-end, y-start and y-end of block calculated from @param blkNr
     *
     * array[4]
     * 1st Dimension: array[0] contains x-start-coordinate
     *                array[1] contains y-start-coordinate
     *                array[2] contains x-end-coordinate
     *                array[3] contains y-end-coordinate
     */
    public int [] getBlkXYStEn(int blkNr){
        int xAxis = getBlkXYPos(blkNr)[0];
        int yAxis = getBlkXYPos(blkNr)[1];
        int xyStEn[] = getBlkXYStEn2(xAxis,  yAxis);
        return xyStEn;
    }

    /** [getNextFreeBlock()]
     * kls::comment:: suggestion: returns the unique identification of a "partition" ?(x/yTLen). The unique identification is represented by a natural number.
     * returns number(int) corresponding to next free block to work on inside blkChain and sets it to 1 (= wip/done)
     */
    public int getNextFreeBlock(){
        synchronized (this.blkChain){
            for(int i = checkPoint; i < blkChain.length; i++){
                if (this.blkChain[i] == 0){
                    if(i == blkChain.length-1){
                        this.blkChain[i] = 1;
                        checkPoint = i;
                        return i;
                    }
                    this.blkChain[i] = 1;
                    checkPoint = i;
                    return i;
                }else{
                    continue;
                }
            }
            return -1;
        }
    }

    /** [getWork()]
     * kls::comment:: the method has no args. (copy/paste "fluechtigkeitsfehler")
     * returns array(int []) containting x-start, x-end, y-start and y-end of block calculated from @param blkNr
     *
     * array[5]
     * 1st Dimension: array[0] contains x-start-coordinate
     *                array[1] contains y-start-coordinate
     *                array[2] contains x-end-coordinate
     *                array[3] contains y-end-coordinate
     *                array[4] contains corresponding block-number
     */
    public int [] getWork(){
        int getNFB = this.getNextFreeBlock();
        int []  workPaket = new int [5];
        int [] tmp = this.getBlkXYStEn(getNFB);

        workPaket[0] = tmp[0];
        workPaket[1] = tmp[1];
        workPaket[2] = tmp[2];
        workPaket[3] = tmp[3];
        workPaket[4] = getNFB;
        return workPaket;
    }
}
