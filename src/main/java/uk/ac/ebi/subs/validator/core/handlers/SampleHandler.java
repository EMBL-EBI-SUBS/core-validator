package uk.ac.ebi.subs.validator.core.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.subs.data.submittable.Sample;
import uk.ac.ebi.subs.validator.core.validators.SampleRefValidator;
import uk.ac.ebi.subs.validator.data.SingleValidationResult;
import uk.ac.ebi.subs.validator.data.SingleValidationResultsEnvelope;
import uk.ac.ebi.subs.validator.data.ValidationMessageEnvelope;

import java.util.Collections;

@Service
public class SampleHandler extends AbstractHandler {

    @Autowired
    SampleRefValidator sampleRefValidator;

    /**
     * A sample may refer to other samples or itself using {@link  uk.ac.ebi.subs.data.component.SampleRelationship SampleRelationship}
     * @param envelope
     * @return
     */
    @Override
    public SingleValidationResultsEnvelope handleValidationRequest(ValidationMessageEnvelope envelope) {
        Sample sample = (Sample) envelope.getEntityToValidate();

        SingleValidationResult singleValidationResult = generateBlankSingleValidationResult(sample.getId(), envelope.getValidationResultUUID());

        sampleRefValidator.validateSampleRelationships(sample.getSampleRelationships(), singleValidationResult);

        SingleValidationResultsEnvelope singleValidationResultsEnvelope = generateSingleValidationResultsEnvelope(envelope, Collections.singletonList(singleValidationResult));

        return singleValidationResultsEnvelope;
    }
}