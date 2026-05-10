package main;

import main.dto.ItemDTO;
import main.dto.PetDTO;
import main.export.CsvExportStrategy;
import main.export.JsonExportStrategy;
import main.export.XmlExportStrategy;
import main.model.service.ExportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExportServiceApplicationTests {

	private ExportService exportService;

	@BeforeEach
	void setUp() {
		exportService = new ExportService();
	}

	private PetDTO samplePet() {
		PetDTO pet = new PetDTO();
		pet.setId(1);
		pet.setName("Buddy");
		pet.setSpecies("Dog");
		pet.setBreed("Labrador");
		pet.setGender("Male");
		pet.setAge(3);
		pet.setPrice(BigDecimal.valueOf(500));
		pet.setAvailable(true);
		return pet;
	}

	private ItemDTO sampleItem() {
		ItemDTO item = new ItemDTO();
		item.setId(1);
		item.setName("Dog Food");
		item.setDescription("Premium food");
		item.setPrice(25.99);
		item.setType("FOOD");
		item.setPetId(1);
		item.setPetName("Buddy");
		return item;
	}


	@Test
	void exportPets_json_shouldReturnValidJson() throws Exception {
		byte[] result = exportService.export(List.of(samplePet()), "json");
		String json = new String(result);
		assertTrue(json.contains("Buddy"));
		assertTrue(json.contains("Labrador"));
	}

	@Test
	void exportPets_csv_shouldReturnCsvWithHeader() throws Exception {
		byte[] result = exportService.export(List.of(samplePet()), "csv");
		String csv = new String(result);
		assertTrue(csv.contains("id,name,species,breed"));
		assertTrue(csv.contains("Buddy"));
	}

	@Test
	void exportPets_xml_shouldReturnXml() throws Exception {
		byte[] result = exportService.export(List.of(samplePet()), "xml");
		String xml = new String(result);
		assertNotNull(xml);
		assertTrue(result.length > 0);
	}


	@Test
	void exportItems_json_shouldReturnValidJson() throws Exception {
		byte[] result = exportService.export(List.of(sampleItem()), "json");
		String json = new String(result);
		assertTrue(json.contains("Dog Food"));
		assertTrue(json.contains("Buddy"));
	}

	@Test
	void exportItems_csv_shouldReturnCsvWithHeader() throws Exception {
		byte[] result = exportService.export(List.of(sampleItem()), "csv");
		String csv = new String(result);
		assertTrue(csv.contains("id,name,description,price,type,petName"));
		assertTrue(csv.contains("Dog Food"));
	}


	@Test
	void export_invalidFormat_shouldThrowException() {
		assertThrows(IllegalArgumentException.class,
				() -> exportService.export(List.of(samplePet()), "pdf"));
	}

	@Test
	void export_emptyList_shouldReturnEmptyContent() throws Exception {
		byte[] result = exportService.export(List.of(), "csv");
		assertNotNull(result);
	}


	@Test
	void csvStrategy_shouldFormatPetsCorrectly() throws Exception {
		CsvExportStrategy strategy = new CsvExportStrategy();
		byte[] result = strategy.export(List.of(samplePet()));
		String csv = new String(result);
		assertTrue(csv.startsWith("id,name,species"));
		assertTrue(csv.contains("Buddy"));
	}

	@Test
	void jsonStrategy_shouldProducePrettyJson() throws Exception {
		JsonExportStrategy strategy = new JsonExportStrategy();
		byte[] result = strategy.export(List.of(samplePet()));
		String json = new String(result);
		assertTrue(json.contains("\n"));
	}

	@Test
	void xmlStrategy_shouldProduceXmlBytes() throws Exception {
		XmlExportStrategy strategy = new XmlExportStrategy();
		byte[] result = strategy.export(List.of(samplePet()));
		assertTrue(result.length > 0);
	}
}
