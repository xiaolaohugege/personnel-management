package com.example.service;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.exception.CustomerException;
import com.example.mapper.AdminMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;

    public List<Admin> getListAdmin(){
        return adminMapper.getListAdmin();
    }

    public List<Admin> getListAdmin(Admin admin){
        return adminMapper.getListAdmin(admin);
    }

    public Admin selectAdminById(Integer id){
        return adminMapper.selectAdminById(id);
    }
    public int addAdmin(Admin admin){

        Admin admin1 = new Admin();
        admin1.setUsername(admin.getUsername());
        List<Admin> adminList = adminMapper.selectAdmin(admin1);
        if(adminList.size() > 0){
            throw new CustomerException("该账号已经被占用，请更换账号");
        }
        return adminMapper.addAdmin(admin);
    }

    public int deleteAdminById(int id){
        return adminMapper.deleteAdminById(id);
    }

    public void deleteBatch(List<Admin> list){
        for (Admin admin : list){
            this.deleteAdminById(admin.getId());
        }
    }

    public PageInfo<Admin> selectPage(Integer pageNum, Integer pageSize, Admin admin) {
        // 开启分页查询
        Account currentUser = TokenUtils.getCurrentUser();
        if("USER".equals(currentUser.getRole())){
            throw new CustomerException("500","您的角色暂无权限执行此操作");
        }

        PageHelper.startPage(pageNum, pageSize);
        List<Admin> list = adminMapper.getListAdmin(admin);
        return PageInfo.of(list);
    }

    public Admin login(Account account) {
        // 验证账号是否存在
        Admin dbAdmin = adminMapper.selectAdminByName(account.getUsername());
        if (dbAdmin == null) {
            throw new CustomerException("账号不存在");
        }
        // 验证密码是否正确
        if (!dbAdmin.getPassword().equals(account.getPassword())) {
            throw new CustomerException("账号或密码错误");
        }
        String token = TokenUtils.createToken(dbAdmin.getId() + "-" + "ADMIN", dbAdmin.getPassword());
        dbAdmin.setToken(token);


        return dbAdmin;
    }


    public void update(Admin admin) {
        adminMapper.update(admin);
    }
}
