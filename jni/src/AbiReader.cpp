
#include "AbiReader.h"

#include "ajseqabi.h"
#include "ajmess.h"
#include "ajfile.h"
#include "ajfileio.h"

#include "AndroidLog.h"

#include <string>
#include <stdlib.h>
#include <stdio.h>
#include <errno.h>

#include <iostream>     // std::cout, std::ios
#include <sstream>      // std::ostringstream

using namespace std;

AbiReader::AbiReader(const std::string &filePath) : filePath(filePath) {

    this->trace          = NULL;
    this->nseq           = NULL;
    this->basePositions  = NULL;

    this->readSeq();
}
//************************************************************************

AbiReader::~AbiReader() {
    this->clearEmbosVars();
}
//************************************************************************

void AbiReader::clearEmbosVars() {
    ajInt2dDel(&this->trace);
    ajStrDel(&this->nseq);
    ajShortDel(&this->basePositions);
}
//*****************************************************************

void AbiReader::printAjInt2d(AjPInt2d val, const char *tag) {
    ajuint len1, len2;
    ajInt2dLen(val, &len1, &len2);

    LOGD("%s Len:%d:%d", tag, len1, len2);

#if 0
    for(ajuint i=0; i<len2; i++) {
        for(ajuint j=0; j<len1; j++) {
            dbg << ajInt2dGet(val, j, i);
        }
        dbg << endl;
    }
#endif
}
//*****************************************************************

void AbiReader::printAjShort(AjPShort arr, const char* tag) {
    ajuint len;
    len = ajShortLen(arr);

    LOGD("%s Len:", tag, len);

    std::ostringstream dbg;
    for (ajuint i=0; i<len; i++) {
        dbg << ajShortGet(arr, i) << " ";
    }
    LOGD("%s", dbg.str().c_str());
}
//*****************************************************************

ajint AbiReader::findTraceMax() {
    ajint res = 0;

    for(ajint iloop=0; iloop<numPoints; iloop++) {
        for(ajint ibase=0; ibase<4; ibase++) {
            if(res < ajInt2dGet(this->trace, ibase, iloop))
                res = ajInt2dGet(this->trace, ibase, iloop);
        }
    }
    return res;
}
//*****************************************************************

ajlong AbiReader::findLastNonTrashPoint() {
    ajlong seqLen = ajShortLen(this->basePositions);
    return ajShortGet(this->basePositions, seqLen-1) + 10;
}
//*****************************************************************

bool AbiReader::isSequenceOk() {
    return this->trace != NULL;
}
//*****************************************************************

bool AbiReader::readSeq() {
    ajlong    numBases;
    ajlong    baseO;
    ajlong    basePosO;
    ajlong    fwo_;
    ajlong    dataOffset[4]  = {0};
    AjPFile   fp             = NULL;
    bool      res;

    /* BYTE[i] is a byte mask for byte i */
    const ajlong BYTE[] = { 0x000000ff };

    LOGD("AbiReader::readSeq()");

    this->clearEmbosVars();

    fp = ajFileNewInNameC(this->filePath.c_str());

    if(!ajSeqABITest(fp))
    	goto end_err_not_abi;

    numBases = ajSeqABIGetNBase(fp);
    baseO = ajSeqABIGetBaseOffset(fp);	/* find BASE tag & get offset */
    this->nseq  = ajStrNew();			/* read in sequence */

    this->trace = ajInt2dNew();
    this->basePositions = ajShortNew();
    this->numPoints = ajSeqABIGetNData(fp);  /* find DATA tag & get no. of points */


    ajSeqABIGetTraceOffset(fp, dataOffset);
    ajSeqABIGetData(fp, dataOffset, this->numPoints, this->trace); /* read in trace data   */

    fwo_ = ajSeqABIGetFWO(fp);	       /* find FWO tag - field order GATC   */
    memset(this->basesOrder, '\0', 5);
    this->basesOrder[0] = (char) (fwo_ >> 24 & BYTE[0]);
    this->basesOrder[1] = (char) (fwo_ >> 16 & BYTE[0]);
    this->basesOrder[2] = (char) (fwo_ >> 8  & BYTE[0]);
    this->basesOrder[3] = (char) (fwo_ & BYTE[0]);

    ajSeqABIReadSeq(fp, baseO, numBases, &this->nseq);
    basePosO = ajSeqABIGetBasePosOffset(fp); /* find PLOC tag & get offset */
    if(ajFileSeek(fp, basePosO, SEEK_SET) != 0)
    	goto end_err_seek;
    ajSeqABIGetBasePosition(fp, numBases, &this->basePositions);

    this->tmax = this->findTraceMax();
    this->lastNonTrashPoint = this->findLastNonTrashPoint();

    LOGD("The Genome Sequence Len:%lu", ajStrGetLen(nseq));

    LOGD("%s", ajFileGetNameC(fp));
    LOGD("base0:%ld, basePosO:%ld", baseO, basePosO);
    LOGD("numPoints:%ld tmax:d", this->numPoints, this->tmax);
    LOGD("lastNonTrashPoint:%ld", this->lastNonTrashPoint);
    LOGD("fwo_:%s", this->basesOrder);

    printAjShort(basePositions, "basePositions");
    printAjInt2d(trace, "trace");

    res = true;
    goto end;

end_err_not_abi:
	LOGE("%s is not abi file", this->filePath.c_str());
	goto end_err;

end_err_seek:
	LOGE("error parsing (seek) abi file %s: %d:%s", this->filePath.c_str(), errno, strerror(errno));
	goto end_err;

end_err:
	this->clearEmbosVars();
	res = false;
	goto end;

end:
    ajFileClose(&fp);
    return res;
}
//*****************************************************************

