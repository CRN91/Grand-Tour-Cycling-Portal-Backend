package src.cycling;

public class CategorisedClimb extends Segment{



    public CategorisedClimb(int stageId, SegmentType segmentType, Double averageGradient,
                            Double length ) {
        super(stageId, segmentType);
        this.averageGradient = averageGradient;
        this.length = length;
    }
}
