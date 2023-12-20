package com.PocFiltering;



import org.apache.tomcat.util.digester.RuleSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ltl-contracts")
public class FilterController {
    @Autowired
    private FilterService filterService;

    @GetMapping("/organization/{orgId}/filter")
    public List<LTLContract> getAllLtlContractsByFilter(@RequestParam Map<String, String> filter, @PathVariable("orgId") long orgId,
                                                                  @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize
    ) {
        try {
             return filterService.getAllLtlContractsByOrgIdAndFilter(orgId, filter, pageNumber, pageSize);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }

    // 2nd approach
    @GetMapping("/organization/{orgId}/filter2")
    public List<LTLContract> getAllLtlContractsByFilters(@RequestParam(required = false) String orgName,
                                                         @RequestParam(required = false) String  orgType,
                                                         @RequestParam(required = false) LocalDate effective,
                                                         @RequestParam(required = false) LocalDate expiry, @PathVariable("orgId") long orgId,
                                                         @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize
    ){
        return filterService.getAllFilteredContracts(orgId,orgName,orgType,effective,expiry,pageNumber,pageSize);
    }


}
