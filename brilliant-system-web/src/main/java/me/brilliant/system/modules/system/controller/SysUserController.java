package me.brilliant.system.modules.system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.brilliant.system.modules.system.model.dto.*;
import me.brilliant.system.modules.system.service.SysUserService;
import me.brilliant.boot.web.mvc.BaseController;
import me.brilliant.boot.web.result.PageResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/10
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "系统用户")
@RequestMapping("/api/sys/user")
public class SysUserController extends BaseController {

    private final SysUserService sysUserService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/page/{pageIndex}/{pageSize}")
    @Operation(summary = "分页查询系统用户")
    @PreAuthorize("@el.check('user:list')")
    public PageResult<SysUserListDto> queryByPage(@Validated @RequestBody SysUserQueryCriteria criteria,
                                                  @PathVariable("pageIndex") int pageIndex,
                                                  @PathVariable("pageSize") int pageSize) {
        PageRequest page = PageRequest.of(pageIndex - 1, pageSize, Sort.Direction.DESC, "createTime");
        return sysUserService.queryByPage(criteria, page);
    }

    @GetMapping("{id}")
    @Operation(summary = "根据id查询系统用户")
    public SysUserDto get(@Parameter(description = "主键Id") @PathVariable("id") Long id) {
        return sysUserService.getById(id);
    }

    @PostMapping("/create")
    @Operation(summary = "新增系统用户")
//    @PreAuthorize("@el.check('user:add')")
    public SysUserDto create(@Validated @RequestBody SysUserForCreateDto createDto) {
        createDto.setPassword(passwordEncoder.encode("123456"));
        return sysUserService.create(createDto);
    }

    @PostMapping("/update")
    @Operation(summary = "修改系统用户信息")
    @PreAuthorize("@el.check('user:edit')")
    public SysUserDto update(@Validated @RequestBody SysUserForUpdateDto updateDto) throws Exception {
        return sysUserService.update(updateDto);
    }

}

