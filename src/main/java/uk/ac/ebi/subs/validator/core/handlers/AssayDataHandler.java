package uk.ac.ebi.subs.validator.core.handlers;

import org.springframework.stereotype.Service;
import uk.ac.ebi.subs.data.submittable.AssayData;
import uk.ac.ebi.subs.validator.core.validators.*;
import uk.ac.ebi.subs.validator.data.AssayDataValidationMessageEnvelope;
import uk.ac.ebi.subs.validator.data.SingleValidationResult;
import uk.ac.ebi.subs.validator.data.ValidationMessageEnvelope;
import uk.ac.ebi.subs.validator.data.structures.ValidationAuthor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class responsible for handle {@link AssayData} validation.
 *
 * An AssayData refers to an Assay via {@link uk.ac.ebi.subs.data.component.AssayRef AssayRef} and to
 * a Sample via {@link uk.ac.ebi.subs.data.component.SampleRef SampleRef}.
 */
@Service
public class AssayDataHandler extends AbstractHandler<AssayDataValidationMessageEnvelope> {


    private ReferenceValidator refValidator;

    private AttributeValidator attributeValidator;

    public AssayDataHandler(ReferenceValidator refValidator,
                            AttributeValidator attributeValidator) {
        this.refValidator = refValidator;
        this.attributeValidator = attributeValidator;
    }

    @Override
    List<SingleValidationResult> validateSubmittable(AssayDataValidationMessageEnvelope envelope) {
        AssayData assayData = getAssayDataFromEnvelope(envelope);

        List<SingleValidationResult> results = Arrays.asList(
                refValidator.validate(assayData.getId(), assayData.getAssayRef(), envelope.getAssay()),
                refValidator.validate(assayData.getId(), assayData.getSampleRef(), envelope.getSample())
        );

        return results;
    }

    @Override
    List<SingleValidationResult> validateAttributes(ValidationMessageEnvelope envelope) {
        AssayData assayData = getAssayDataFromEnvelope(envelope);

        return ValidatorHelper.validateAttribute(assayData.getAttributes(), assayData.getId(), attributeValidator);
    }

    private AssayData getAssayDataFromEnvelope(ValidationMessageEnvelope envelope) {
        return (AssayData) envelope.getEntityToValidate();
    }

}
