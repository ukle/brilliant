package me.brilliant.system.modules.system.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.brilliant.boot.web.mvc.BaseController;
import me.brilliant.boot.web.plugin.norepeat.PreventRepeatSubmit;
import me.brilliant.boot.web.result.PageResult;
import me.brilliant.system.modules.system.model.dto.SysUserDto;
import me.brilliant.system.modules.system.model.dto.SysUserForCreateDto;
import me.brilliant.system.modules.system.model.dto.SysUserForUpdateDto;
import me.brilliant.system.modules.system.model.dto.SysUserQueryCriteria;
import me.brilliant.system.modules.system.model.vo.UserInfoVO;
import me.brilliant.system.modules.system.model.vo.UserPageVO;
import me.brilliant.system.modules.system.service.SysUserService;
import me.brilliant.system.plugins.LogAnnotation;
import me.brilliant.system.plugins.LogModuleEnum;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/10
 */
@Tag(name = "02.用户接口")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class SysUserController extends BaseController {

    private final SysUserService userService;

    @Operation(summary = "用户分页列表")
    @PostMapping("/page/{pageIndex}/{pageSize}")
    @LogAnnotation(value = "用户分页列表", module = LogModuleEnum.USER)
    public PageResult<UserPageVO> listPagedUsers(@RequestBody SysUserQueryCriteria queryCriteria,
                                                 @PathVariable("pageIndex") int pageIndex,
                                                 @PathVariable("pageSize") int pageSize) {
        PageRequest page = PageRequest.of(pageIndex - 1, pageSize, Sort.Direction.DESC, "createTime");
        return userService.queryByPage(queryCriteria, page);
    }

    @Operation(summary = "新增用户")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('sys:user:add')")
    @PreventRepeatSubmit
    public void saveUser(@RequestBody @Valid SysUserForCreateDto createDto) {
        userService.create(createDto);
    }

    @Operation(summary = "用户表单数据")
    @GetMapping("/{userId}/form")
    public SysUserDto getUserForm(@Parameter(description = "用户ID") @PathVariable Long userId) {
        return userService.getById(userId);
    }

    @Operation(summary = "修改用户")
    @PutMapping(value = "/{userId}")
    @PreAuthorize("@ss.hasPerm('sys:user:edit')")
    public void updateUser(@Parameter(description = "用户ID") @PathVariable Long userId,
                           @RequestBody @Validated SysUserForUpdateDto updateDto) {
        userService.update(updateDto);
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('sys:user:delete')")
    public void deleteUsers(@Parameter(description = "用户ID，多个以英文逗号(,)分割") @PathVariable String ids) {
        userService.deleteUsers(ids);
    }

    @Operation(summary = "修改用户密码")
    @PatchMapping(value = "/{userId}/password")
    @PreAuthorize("@ss.hasPerm('sys:user:password:reset')")
    public void updatePassword(@Parameter(description = "用户ID") @PathVariable Long userId,
                               @RequestParam String password) {
        userService.updatePassword(userId, password);
    }

    @Operation(summary = "修改用户状态")
    @PatchMapping(value = "/{userId}/status")
    public void updateUserStatus(@Parameter(description = "用户ID") @PathVariable Long userId,
                                 @Parameter(description = "用户状态(1:启用;0:禁用)") @RequestParam Integer status) {
        userService.updateStatus(userId, status);
    }

    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/me")
    public UserInfoVO getCurrentUserInfo() {
        return userService.getCurrentUserInfo();
    }

    @Operation(summary = "用户导入模板下载")
    @GetMapping("/template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        String fileName = "用户导入模板.xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));

        String fileClassPath = "excel-templates" + File.separator + fileName;
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileClassPath);

        ServletOutputStream outputStream = response.getOutputStream();
        ExcelWriter excelWriter = EasyExcel.write(outputStream).withTemplate(inputStream).build();

        excelWriter.finish();
    }
/*
    @Operation(summary = "导入用户")
    @PostMapping("/import")
    public String importUsers(MultipartFile file) throws IOException {
        UserImportListener listener = new UserImportListener();
        String msg = ExcelUtils.importExcel(file.getInputStream(), UserImportDTO.class, listener);
        return msg;
    }

    @Operation(summary = "导出用户")
    @GetMapping("/export")
    public void exportUsers(UserPageQuery queryParams, HttpServletResponse response) throws IOException {
        String fileName = "用户列表.xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));

        List<UserExportDTO> exportUserList = userService.listExportUsers(queryParams);
        EasyExcel.write(response.getOutputStream(), UserExportDTO.class).sheet("用户列表")
                .doWrite(exportUserList);
    }*/
}

