<script setup lang="ts">
import { changePassword, getUserInfo, setUserAvatar } from '@/api/userApi';
import BackToHome from '@/components/BackToHome.vue';
import FormInput from '@/components/form/FormInput.vue';
import FormItem from '@/components/form/FormItem.vue';
import SaveModal from '@/components/SaveModal.vue';
import { useToast } from '@/components/toast';
import UserAvatar from '@/components/UserAvatar.vue';
import { User } from '@/interface/user';
import { useUserStore } from '@/stores/useUserStore';
import {ref, watch} from 'vue';
import { useRouter } from 'vue-router';
import {useGlobalConfigStore} from "@/stores/useGlobalConfigStore.ts";
import {saveGlobalConfig} from "@/api/globalConfigApi.ts";

const toast = useToast();
const router = useRouter();
const { logout } = useUserStore();

const user = ref<User | any>();

const fetchUser = async () => {
  const result = await getUserInfo(null);
  if (!result.success) {
    toast.error(result.message);
    return
  }
  user.value = result.data;
  userForm.value.username = user.value.username;
  if (user.value.avatar?.length < 5 * 1024) {
    userForm.value.avatar = user.value.avatar;
  }
}

fetchUser();


const openModal = ref({ add: false, set: false });
const openAvatarModal = ref({ add: false, set: false });
const openConfigModal = ref({ add: false, set: false });
const confirmLogout = ref(false);

const userForm = ref({
  username: '',
  oldPassword: '',
  newPassword: '',
  confirmPassword: '',
  avatar: '',
});
const handleSubmit = async () => {
  const { newPassword, confirmPassword } = userForm.value;
  if (newPassword !== confirmPassword) {
    toast.error('两次输入的密码不一致');
    return;
  }

  const data = { 
    username: userForm.value.username,
    oldPassword: userForm.value.oldPassword,
    newPassword: userForm.value.newPassword,
   }
  const result = await changePassword(data);
  if (result.success) {
    toast.success('修改账号密码成功');
    userForm.value.oldPassword = '';
    userForm.value.newPassword = '';
    userForm.value.confirmPassword = '';
    openModal.value.set = false;
    logout(() => router.push('/login'));
  } else {
    toast.error(result.message);
  }
};


const uploadLocal = ref(true);
const uploadAvatar = (event: Event) => {
  // 读取成 base64
  const file = (event.target as HTMLInputElement).files?.[0];
  if (!file) {
    return;
  }
  const reader = new FileReader();
  reader.readAsDataURL(file);
  reader.onload = (e) => {
    userForm.value.avatar = (e.target as FileReader).result as string;
  }

}

const saveAvatar = async () => {
  const { avatar } = userForm.value;
  if (avatar === null || avatar === undefined) {
    toast.error('请选择头像');
    return;
  }
  const data = { 
    avatar: userForm.value.avatar,
   }
  const result = await setUserAvatar(data);
  if (result.success) {
    toast.success('修改头像成功');
    openAvatarModal.value.set = false;
    fetchUser();
  } else {
    toast.error(result.message);
  }
}

const handleLogout = () => {
  if (!confirmLogout.value) {
    confirmLogout.value = true;
    setTimeout(() => {
      confirmLogout.value = false;
    }, 3000);
    return;
  }
  confirmLogout.value = false;
  logout(() => router.push('/'))
}

let {state, refresh} = useGlobalConfigStore();
const configForm = ref<any>({...state.config});
watch(()=> state.config, (value)=>{
  configForm.value = value
})
const handleSaveConfig = async () => {
  const result = await saveGlobalConfig({...configForm.value});
  if (result.success) {
    refresh()
    toast.success(result.message)
    openConfigModal.value.add = false
  } else {
    toast.error(result.message)
  }

}
</script>

<template>
  <div class="flex w-full h-full flex-col justify-center items-center">
    <div class="flex justify-between w-full">
      <span class="text-3xl font-bold">用户信息</span>
      <BackToHome />
    </div>
    <div class="flex flex-col gap-2">
      <div class="flex justify-center">
        <UserAvatar size="large" :avatar="user?.avatar" @click="openAvatarModal.set = true" />
      </div>

      <div class="flex flex-col gap-4 text-2xl text-center my-8">
        <span>UID: {{ user?.id }}</span>
        <span>账号: {{ user?.username }}</span>
        <span>角色: {{ user?.role }}</span>
        <span>是否启用: {{ user?.enabled }}</span>
        <span>注册时间: {{ user?.createTime }}</span>
      </div>
    </div>
    <div class="mt-2 flex gap-8 text-gray-200 font-bold">
      <button @click="openConfigModal.add = true" class="bg-sky-500 hover:bg-sky-700 py-2 px-4 rounded">更多设置</button>
      <button @click="openModal.add = true" class="bg-sky-500 hover:bg-sky-700 py-2 px-4 rounded">修改账号密码</button>
      <!-- <button @click="fetchUser" class="bg-red-500 hover:bg-red-700 py-2 px-4 rounded">修改密码</button> -->
      <button @click="handleLogout"
        class="bg-gray-500 hover:bg-gray-700 py-2 px-4 rounded"
        :class="{ 'text-red-300': confirmLogout }">
        {{ confirmLogout? '确认退出' : '退出登录' }}
      </button>
    </div>


    <SaveModal name="修改账号密码" :open-modal="openModal" @on-close="() => (openModal.add = false)"
      @on-submit="handleSubmit">
      <template #form>
        <FormItem label="用户名">
          <FormInput v-model="userForm.username" />
        </FormItem>
        <FormItem label="请输入旧密码">
          <FormInput v-model="userForm.oldPassword" type="password" />
        </FormItem>
        <FormItem label="新密码">
          <FormInput v-model="userForm.newPassword" type="password" />
        </FormItem>
        <FormItem label="确认新密码">
          <FormInput v-model="userForm.confirmPassword" type="password" />
        </FormItem>
      </template>
    </SaveModal>

    <SaveModal name="修改头像" :open-modal="openAvatarModal" @on-close="() => (openAvatarModal.set = false)"
      @on-submit="saveAvatar">
      <template #form>

        <div class="flex gap-2 flex-row mt-2">
          <span class="cursor-pointer hover:text-sky-400" :class="{ 'text-sky-500': uploadLocal }" @click="uploadLocal = true">本地上传</span>
          <span class="cursor-pointer hover:text-sky-400" :class="{ 'text-sky-500': !uploadLocal }" @click="uploadLocal = false">网络地址/svg/base64</span>
        </div>
        <FormItem>
          <input v-if="uploadLocal" type="file" @change="uploadAvatar" class="border border-gray-300 rounded-md w-full py-2 px-4 text-gray-700 leading-tight focus:outline-none focus:border-sky-500" />
          <FormInput v-else v-model="userForm.avatar" />
        </FormItem>
      </template>
    </SaveModal>

    <SaveModal name="更多设置" :open-modal="openConfigModal" @on-close="openConfigModal.add=false"
      @on-submit="handleSaveConfig">
      <template #form>
        <FormItem label="背景图片">
          <FormInput v-model="configForm.bgUrl"/>
        </FormItem>
      </template>
    </SaveModal>

  </div>
</template>