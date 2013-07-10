#ifndef __ABI_READER_H__
#define __ABI_READER_H__

#include <string>
#include "ajarch.h"
#include "ajarr.h"
#include "ajstr.h"

class AbiReader {
public:
    AjPInt2d  trace;
    AjPStr    nseq;
    AjPShort  basePositions;
    ajlong    numPoints;
    ajlong    lastNonTrashPoint;
    ajint     tmax;                // Max signal strength
    char      basesOrder[5];       // Sequnce bases order

	AbiReader(const std::string &filePath);
	~AbiReader();

    /**
     * @return true if genome file sequence was successfully loaded.
     */
    bool isSequenceOk();

private:

	const std::string filePath;

    static void printAjInt2d(AjPInt2d val, const char *tag);
    static void printAjShort(AjPShort arr, const char* tag);

    bool readSeq();

    ajint  findTraceMax();
    ajlong findLastNonTrashPoint();

    /**
     * Frees memory of class variable for emboss library.
     * P.S. All variables will be point to NULL.
     */
    void clearEmbosVars();
};

#endif
