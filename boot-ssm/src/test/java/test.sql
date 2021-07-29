-- 根据用户名查询角色(两张表关联)
select id, name
from role
       inner join user_role on role.id = user_role.role_id
where user_role.user_id = 3;

-- 根据角色查询权限列表（两张表关联）
select name
from permission
       inner join role_permission on permission.id = role_permission.permission_id
where role_permission.role_id = 3;

-- 直接根据用户查询具有的权限（三张表关联）
select permission.name
from permission
       inner join role_permission on permission.id = role_permission.permission_id
       inner join user_role on role_permission.role_id = user_role.role_id
where user_role.user_id = 2;
