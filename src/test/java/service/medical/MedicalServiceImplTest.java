package service.medical;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.medical.MedicalService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.mock;

public class MedicalServiceImplTest {

    private static PatientInfoRepository patientInfoRepository;
    private static SendAlertService sendAlertService;
    private static MedicalService medicalService;
    private static final String MESSAGE = "Warning, patient with id: id1, need help";
    private static final String ID1 = "id1";


    @Test
    public void testCheckBloodPressureLow() {
        PatientInfo patientInfo = new PatientInfo("id1", "Ivan", "Ivanov",
                LocalDate.of(1999, 07, 23), new HealthInfo(new BigDecimal(36.6),
                new BloodPressure(120, 80)));
        patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoRepository.getById(ID1)).thenReturn((patientInfo));
        sendAlertService = mock(SendAlertService.class);
        medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkBloodPressure(ID1, new BloodPressure(100, 80));
        Mockito.verify(sendAlertService, Mockito.times(1)).send(MESSAGE);
    }

    @Test
    public void testCheckTemperature() {
        PatientInfo patientInfo = new PatientInfo("id1", "Ivan", "Ivanov",
                LocalDate.of(1999, 07, 23), new HealthInfo(new BigDecimal(36.6),
                new BloodPressure(120, 80)));
        patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoRepository.getById(ID1)).thenReturn((patientInfo));
        sendAlertService = mock(SendAlertService.class);
        medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkTemperature(ID1, new BigDecimal(34));
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.verify(sendAlertService).send(argumentCaptor.capture());
        Assertions.assertEquals(MESSAGE, argumentCaptor.getValue());
    }

    @Test
    public void testPresAndTempNorm() {
        PatientInfo patientInfo = new PatientInfo("id1", "Ivan", "Ivanov",
                LocalDate.of(1999, 07, 23), new HealthInfo(new BigDecimal(36.6),
                new BloodPressure(120, 80)));
        patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoRepository.getById(ID1)).thenReturn((patientInfo));
        sendAlertService = mock(SendAlertService.class);
        medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkBloodPressure(ID1, new BloodPressure(120, 80));
        medicalService.checkTemperature(ID1, new BigDecimal(36.6));

        Mockito.verify(sendAlertService, Mockito.times(0)).send(MESSAGE);
    }


}