package uk.ac.ebi.subs.validator.core.validators;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.subs.data.component.SampleRelationship;
import uk.ac.ebi.subs.repository.model.Sample;
import uk.ac.ebi.subs.repository.repos.submittables.SampleRepository;
import uk.ac.ebi.subs.validator.data.SingleValidationResult;
import uk.ac.ebi.subs.validator.data.ValidationAuthor;
import uk.ac.ebi.subs.validator.data.ValidationStatus;

import java.util.Arrays;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SampleRelationshipValidatorTest {

    private SampleRelationshipValidator validator;
    private SampleRepository repository;

    private SampleRelationship relationship;
    private SingleValidationResult singleValidationResult;

    @Before
    public void setUp() {
        validator = new SampleRelationshipValidator();
        repository = mock(SampleRepository.class);
        validator.repository = repository;

        when(repository.findByAccession("SAMEA100001")).thenReturn(null);
        when(repository.findByAccession("SAMEA123456")).thenReturn(new Sample());

        relationship = generateSampleRelationship();
        singleValidationResult = generateSingleValidationResult("123456");
    }

    @Test
    public void referenceNotFoundTest() {
        validator.validate(Arrays.asList(relationship), singleValidationResult);
        System.out.println(singleValidationResult.getMessage());
        Assert.assertEquals(ValidationStatus.Error, singleValidationResult.getValidationStatus());
    }

    @Test
    public void referenceFoundTest() {
        relationship.setAccession("SAMEA123456");
        validator.validate(Arrays.asList(relationship), singleValidationResult);
        System.out.println(singleValidationResult.getMessage());

        Assert.assertEquals(ValidationStatus.Pass, singleValidationResult.getValidationStatus());
    }

    private SingleValidationResult generateSingleValidationResult(String entityId) {
        SingleValidationResult result = new SingleValidationResult();
        result.setUuid(UUID.randomUUID().toString());
        result.setEntityUuid(entityId);
        result.setValidationAuthor(ValidationAuthor.Core);
        return result;
    }

    private SampleRelationship generateSampleRelationship() {
        SampleRelationship relationship = new SampleRelationship();
        relationship.setAccession("SAMEA100001");
        return relationship;
    }

}