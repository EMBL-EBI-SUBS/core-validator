package uk.ac.ebi.subs.validator.core.handlers;

import org.springframework.stereotype.Service;
import uk.ac.ebi.subs.data.submittable.Assay;
import uk.ac.ebi.subs.validator.core.validators.AttributeValidator;
import uk.ac.ebi.subs.validator.core.validators.SampleRefValidator;
import uk.ac.ebi.subs.validator.core.validators.StudyRefValidator;
import uk.ac.ebi.subs.validator.core.validators.ValidatorHelper;
import uk.ac.ebi.subs.validator.data.SingleValidationResult;
import uk.ac.ebi.subs.validator.data.ValidationMessageEnvelope;
import uk.ac.ebi.subs.validator.data.structures.ValidationAuthor;

import java.util.List;

/**
 * This class responsible for handle {@link Assay} validation.
 *
 * An assay refers to a study via {@link uk.ac.ebi.subs.data.component.StudyRef StudyRef} and to
 * one or multiple samples via {@link uk.ac.ebi.subs.data.component.SampleUse SampleUse}.
 */
@Service
public class AssayHandler extends AbstractHandler {

    private StudyRefValidator studyRefValidator;

    private SampleRefValidator sampleRefValidator;

    private AttributeValidator attributeValidator;

    public AssayHandler(StudyRefValidator studyRefValidator, SampleRefValidator sampleRefValidator,
                        AttributeValidator attributeValidator) {
        this.studyRefValidator = studyRefValidator;
        this.sampleRefValidator = sampleRefValidator;
        this.attributeValidator = attributeValidator;
    }

    @Override
    SingleValidationResult validateSubmittable(ValidationMessageEnvelope envelope) {
        Assay assay = getAssayFromEnvelope(envelope);

        SingleValidationResult singleValidationResult = new SingleValidationResult(ValidationAuthor.Core, assay.getId());
        studyRefValidator.validate(assay.getStudyRef(), singleValidationResult);
        sampleRefValidator.validateSampleUses(assay.getSampleUses(), singleValidationResult);

        return singleValidationResult;
    }

    @Override
    List<SingleValidationResult> validateAttributes(ValidationMessageEnvelope envelope) {
        Assay assay = getAssayFromEnvelope(envelope);

        return ValidatorHelper.validateAttribute(assay.getAttributes(), assay.getId(), attributeValidator);
    }

    private Assay getAssayFromEnvelope(ValidationMessageEnvelope envelope) {
        return (Assay) envelope.getEntityToValidate();
    }
}
