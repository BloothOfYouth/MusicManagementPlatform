<template>
    <div class="table">

        <div class="container">
            <div class="handle-box">
                <el-button type="danger" @click="delSome">批量删除</el-button>
                <el-input v-model="queryInfo.query" placeholder="请输入用户名称" class="handle-input" @keyup.enter.native="getData">
                    <el-button slot="append" icon="el-icon-search" @click="getData"></el-button>
                </el-input>
                <el-button type="primary" @click="centerDialogVisible = true">添加用户</el-button>
            </div>
        </div>

        <el-table border style="width: 100%" height="680" stripe :data="users" @selection-change="handleSelectionChange" ref="userTable">
            <el-table-column type="selection" width="40px"></el-table-column>
            <el-table-column label="用户头像" width="150px" align="center">
                <template slot-scope="scope">
                    <div class="user-img">
                        <el-image :src="HOST+scope.row.avator" :preview-src-list="[HOST+scope.row.avator]"></el-image>
                    </div>
                    <el-upload :action="uploadUrl(scope.row.id)" :before-upload="beforeAvatorUpload"
                               :on-success="handleAvatorSuccess" accept=".jpeg,.png,.jpg" ref="uploadPic">
                        <el-button size="mini" round icon="el-icon-picture-outline">更新头像</el-button>
                    </el-upload>
                </template>
            </el-table-column>
            <el-table-column prop="name" label="用户名" width="120px" align="center"></el-table-column>
            <el-table-column label="性别" align="center" width="50px">
                <template slot-scope="scope">
                    {{ changeSex(scope.row.sex) }}
                </template>
            </el-table-column>
            <el-table-column prop="phoneNum" label="手机号" width="120px" align="center"></el-table-column>
            <el-table-column prop="email" label="邮箱" width="120px" align="center"></el-table-column>
            <el-table-column label="生日" width="100px" align="center">
                <template slot-scope="scope">
                    {{scope.row.birth | formatDate }}
                </template>
            </el-table-column>
            <el-table-column label="签名" align="center">
                <template slot-scope="scope">
                    <p style="height: 100px;overflow: auto">{{ scope.row.introduction }}</p>
                </template>
            </el-table-column>
            <el-table-column prop="location" label="地区" width="100px" align="center"></el-table-column>
            <el-table-column label="操作" align="center" width="170px">
                <template slot-scope="scope">
                    <el-button type="primary" icon="el-icon-edit" round size="small" @click="handleEdit(scope.row.id)">编辑</el-button>
                    <el-popconfirm
                            confirm-button-text='好的'
                            cancel-button-text='不用了'
                            icon="el-icon-info"
                            icon-color="red"
                            :title="'确定要删除[ '+scope.row.name+' ]用户?'"
                            @confirm="handleDelete(scope.row.id)"
                    >
                        <el-button slot="reference" type="danger" icon="el-icon-delete" round size="small">删除</el-button>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>

        <div class="paging">
            <el-pagination
                    background
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="queryInfo.pageNum"
                    :page-sizes="[3, 5, 10]"
                    :page-size="queryInfo.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
            </el-pagination>
        </div>

        <el-dialog title="添加用户" :visible.sync="centerDialogVisible" width="400px" center="center" @close="cancelAdd">
            <el-form :model="registerForm" :rules="registerFormRule" ref="registerFormRef" label-width="65px" @keyup.enter.native="addUser">
                <el-form-item prop="name" label="用户名" size="mini">
                    <el-input v-model="registerForm.name" placeholder="用户名"></el-input>
                </el-form-item>
                <el-form-item prop="username" label="账号" size="mini">
                    <el-input v-model="registerForm.username" placeholder="账号"></el-input>
                </el-form-item>
                <el-form-item prop="password" label="密码" size="mini">
                    <el-input type="password" v-model="registerForm.password" placeholder="密码"></el-input>
                </el-form-item>
                <el-form-item prop="sex" label="性别" size="mini">
                    <el-radio-group v-model="registerForm.sex">
                        <el-radio :label="0">女</el-radio>
                        <el-radio :label="1">男</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item prop="phoneNum" label="手机号" size="mini">
                    <el-input v-model="registerForm.phoneNum" oninput="if(isNaN(value)) { value = null } if(value.indexOf('.')>0){value=value.slice(0,value.indexOf('.')+3)}" placeholder="手机号"></el-input>
                </el-form-item>
                <el-form-item prop="email" label="邮箱" size="mini">
                    <el-input v-model="registerForm.email" type="email" placeholder="邮箱"></el-input>
                </el-form-item>
                <el-form-item prop="birth" label="生日" size="mini">
                    <el-date-picker v-model="registerForm.birth" type="date" placeholder="选择日期" style="width: 100%"></el-date-picker>
                </el-form-item>
                <el-form-item prop="location" label="地区" size="mini">
                    <el-input v-model="registerForm.location" placeholder="地区"></el-input>
                </el-form-item>
                <el-form-item prop="introduction" label="签名" size="mini">
                    <el-input v-model="registerForm.introduction" placeholder="签名" type="textarea"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer">
                <el-button @click="cancelAdd">取 消</el-button>
                <el-button type="primary" @click="addUser">确 定</el-button>
            </span>
        </el-dialog>

        <el-dialog title="编辑用户" :visible.sync="editDialogVisible" width="400px" center="center">
            <el-form :model="editForm" :rules="registerFormRule" ref="editFormRef" label-width="65px" @keyup.enter.native="editUser">
                <el-form-item prop="name" label="用户名" size="mini">
                    <el-input v-model="editForm.name" placeholder="用户名"></el-input>
                </el-form-item>
                <el-form-item prop="username" label="账号" size="mini">
                    <el-input v-model="editForm.username" placeholder="账号"></el-input>
                </el-form-item>
                <el-form-item prop="password" label="密码" size="mini">
                    <el-input type="password" v-model="editForm.password" placeholder="密码"></el-input>
                </el-form-item>
                <el-form-item prop="sex" label="性别" size="mini">
                    <el-radio-group v-model="editForm.sex">
                        <el-radio :label="0">女</el-radio>
                        <el-radio :label="1">男</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item prop="phoneNum" label="手机号" size="mini">
                    <el-input v-model="editForm.phoneNum" oninput="if(isNaN(value)) { value = null } if(value.indexOf('.')>0){value=value.slice(0,value.indexOf('.')+3)}" placeholder="手机号"></el-input>
                </el-form-item>
                <el-form-item prop="email" label="邮箱" size="mini">
                    <el-input v-model="editForm.email" type="email" placeholder="邮箱"></el-input>
                </el-form-item>
                <el-form-item prop="birth" label="生日" size="mini">
                    <el-date-picker v-model="editForm.birth" type="date" placeholder="选择日期" style="width: 100%"></el-date-picker>
                </el-form-item>
                <el-form-item prop="location" label="地区" size="mini">
                    <el-input v-model="editForm.location" placeholder="地区"></el-input>
                </el-form-item>
                <el-form-item prop="introduction" label="签名" size="mini">
                    <el-input v-model="editForm.introduction" placeholder="签名" type="textarea"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer">
                <el-button @click="editDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="editUser">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import { me } from '../assets/js/me.js'
    export default {
        name: "UserPage",
        data() {
            return {
                centerDialogVisible: false,
                editDialogVisible: false,
                registerForm: {
                    name: '',
                    username: '',
                    password: '',
                    sex: '',
                    phoneNum: '',
                    email: '',
                    pic: '',
                    birth: '',
                    location: '',
                    introduction: '',
                },
                editForm: {

                },
                users: [

                ],
                queryInfo: {
                    query: '',
                    pageNum: 1,
                    pageSize: 5
                },
                total: 0,
                registerFormRule: {
                    name: [
                        { required: true, message: '用户名为必填项', trigger: 'blur' },
                    ],
                    username: [
                        { required: true, message: '账号为必填项', trigger: 'blur' },
                    ],
                    password: [
                        { required: true, message: '密码为必填项', trigger: 'blur' },
                    ],
                    sex: [
                        { required: true, message: '性别为必填项', trigger: 'blur' },
                    ],
                    birth: [
                        { required: true, message: '生日为必填项', trigger: 'blur' },
                    ],
                    introduction: [
                        { required: true, message: '签名为必填项', trigger: 'blur' },
                    ]
                },
                HOST: this.$http.defaults.baseURL,
                multipleSelection: [],  // 哪些选项打勾
            }
        },
        methods: {
            addUser() {
                this.$refs.registerFormRef.validate(valid => {
                    if (valid) {
                        this.$http.post("user/add",this.registerForm).then(res => {
                            if (res.data.result == "success"){
                                this.$notify({
                                    title: "成功",
                                    message: res.data.message,
                                    type: "success"
                                })
                                this.getData()
                                this.$refs.registerFormRef.resetFields()
                            }else {
                                this.$notify({
                                    title: "错误",
                                    message: res.data.message,
                                    type: "error"
                                })
                            }
                        })
                    }else {
                        return;
                    }
                    this.centerDialogVisible = false
                })
            },
            cancelAdd() {
                this.$refs.registerFormRef.resetFields()
                this.centerDialogVisible = false
            },
            getData() {
                this.$http.post("user/get",this.queryInfo).then(res => {
                    this.users = res.data.list
                    this.total = res.data.total
                    this.queryInfo.pageNum = res.data.pageNum
                    if (this.users != null && this.users.length == 0 && this.queryInfo.pageNum != 1) {
                        this.queryInfo.pageNum = 1
                        this.getData()
                    }
                })
            },
            changeSex(value) {
                if (value == 0) {
                    return '女'
                }
                if (value == 1) {
                    return '男'
                }
                if (value == 2) {
                    return '组合'
                }
                if (value == 3) {
                    return '不明'
                }
                return value
            },
            uploadUrl(id) {
                return this.HOST+'/user/updateUserAvator/'+id;
            },
            beforeAvatorUpload(file) {
                me.methods.beforeAvatorUpload(file,this)
            },
            handleAvatorSuccess(res) {
                me.methods.handleFileSuccess(res,this)
            },
            // 改变最大显示数触发动作
            handleSizeChange(newPageSize) {
                this.queryInfo.pageSize = newPageSize;
                this.getData();
            },
            // 当前页触发动作
            handleCurrentChange(newPageNum) {
                this.queryInfo.pageNum = newPageNum;
                this.getData();
            },
            handleEdit(id) {
                this.editDialogVisible = true
                this.$http.get("user/getOne/"+id).then(res => {
                    if (res.data.result == "success") {
                        this.editForm = res.data.message
                    }
                })
            },
            editUser() {
                this.$refs.editFormRef.validate(valid => {
                    if (valid) {
                        this.$http.put("user/update",this.editForm).then(res => {
                            if (res.data.result == "success"){
                                this.$notify({
                                    title: "成功",
                                    message: res.data.message,
                                    type: "success"
                                })
                                this.getData()
                            }else {
                                this.$notify({
                                    title: "错误",
                                    message: res.data.message,
                                    type: "error"
                                })
                            }
                        })
                    }else {
                        return;
                    }
                    this.editDialogVisible = false
                })
            },
            handleDelete(id) {
                this.$http.delete("user/delete/"+id).then(res => {
                    if (res.data.result == "success"){
                        this.$notify({
                            title: "成功",
                            message: res.data.message,
                            type: "success"
                        })
                        this.getData()
                    }else {
                        this.$notify({
                            title: "错误",
                            message: res.data.message,
                            type: "error"
                        })
                    }
                })
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            delSome() {
                if (this.multipleSelection.length == 0) {
                    this.$alert("请选择要批量删除的选项","提示",{
                        type: "info ",
                        confirmButtonText: "确定"
                    })
                    return;
                }
                this.$confirm('确定要批量删除吗？',{
                    type: "warning"
                }).then(ok => {
                    let ids = []
                    for (let item of this.multipleSelection) {
                        ids.push(item.id)
                    }
                    this.$http.post("user/delSome",{"ids":ids}).then(res => {
                        if (res.data.result == "success"){
                            this.$notify({
                                title: "成功",
                                message: res.data.message,
                                type: "success"
                            })
                            this.getData()
                        }else {
                            this.$notify({
                                title: "错误",
                                message: res.data.message,
                                type: "error"
                            })
                        }
                    })
                    this.multipleSelection = []
                }).catch(no => {

                })
            },
            songEdit(id) {
                this.$router.push({
                    name: "SongPage",
                    params: {id: id}
                })
            },
            checkHaveSong(item) {
                if (item.songCount != 0) {
                    return true
                }else {
                    return false
                }
            },
            canSelect(row) {
                let flag = !this.checkHaveSong(row);
                return flag
            }
        },
        created() {
            this.getData()
        },
        watch: {

        }
    }
</script>

<style scoped lang="less">
    .handle-box {
        margin-bottom: 20px;
    }
    .user-img {
        width: 100%;
        height: 87px;
        border-radius: 5px;
        margin-bottom: 5px;
        overflow: hidden;
        .el-image {
            width: 100%;
            height: 100%;
        }
    }
    .handle-input {
        width: 500px;
        margin-right: 20px;
        margin-left: 20px;
    }
    .paging {
        display: flex;
        justify-content: center;
        margin-top: 15px;
    }
</style>
