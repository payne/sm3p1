package org.mattpayne.learning.sm3p1.application;

import java.util.List;
import org.mattpayne.learning.sm3p1.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationService(final ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public List<ApplicationDTO> findAll() {
        final List<Application> applications = applicationRepository.findAll(Sort.by("id"));
        return applications.stream()
                .map((application) -> mapToDTO(application, new ApplicationDTO()))
                .toList();
    }

    public ApplicationDTO get(final Long id) {
        return applicationRepository.findById(id)
                .map(application -> mapToDTO(application, new ApplicationDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ApplicationDTO applicationDTO) {
        final Application application = new Application();
        mapToEntity(applicationDTO, application);
        return applicationRepository.save(application).getId();
    }

    public void update(final Long id, final ApplicationDTO applicationDTO) {
        final Application application = applicationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(applicationDTO, application);
        applicationRepository.save(application);
    }

    public void delete(final Long id) {
        applicationRepository.deleteById(id);
    }

    private ApplicationDTO mapToDTO(final Application application,
            final ApplicationDTO applicationDTO) {
        applicationDTO.setId(application.getId());
        applicationDTO.setEmail(application.getEmail());
        applicationDTO.setAge(application.getAge());
        applicationDTO.setAmount(application.getAmount());
        return applicationDTO;
    }

    private Application mapToEntity(final ApplicationDTO applicationDTO,
            final Application application) {
        application.setEmail(applicationDTO.getEmail());
        application.setAge(applicationDTO.getAge());
        application.setAmount(applicationDTO.getAmount());
        return application;
    }

    public boolean emailExists(final String email) {
        return applicationRepository.existsByEmailIgnoreCase(email);
    }

}
