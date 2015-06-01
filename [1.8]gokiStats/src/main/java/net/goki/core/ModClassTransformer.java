package net.goki.core;

import java.util.Arrays;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import static org.objectweb.asm.Opcodes.*;
import net.minecraft.launchwrapper.IClassTransformer;
import net.goki.lib.Reference;

public class ModClassTransformer implements IClassTransformer
{
	boolean isObfscated;
	
	private static final String[] classList = 
		{
			"net.goki.handlers.TickHander"
		};

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) 
	{
		isObfscated = !name.equals(transformedName);
		int index = -1;
		if(Reference.isPlayerAPILoaded)
		{
			index = Arrays.asList(classList).indexOf(transformedName);
		}
		return index == -1? basicClass: handle(index,basicClass);
	}

	private byte[] handle(int index, byte[] basicClass) 
	{

		System.out.println("Handling");
		try
		{
			ClassNode classNode = new ClassNode();
			ClassReader reader = new ClassReader(basicClass);
			reader.accept(classNode,0);
			
			switch(index)
			{
			case 0:
				transformTickHandler(classNode);
				break;
			}
			
			ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
			classNode.accept(writer);
			return writer.toByteArray();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return basicClass;
	}
	
	private void transformTickHandler(ClassNode classNode)
	{
		for(MethodNode method : classNode.methods)
		{
			AbstractInsnNode target = null;
			
			findInWater(method,target);
			handleTick(method,target);
		}
	}

	private void findInWater(MethodNode method, AbstractInsnNode target)
	{
		final String playerTickName = isObfscated? "" : "playerTick";
		final String playerTickDescripter = isObfscated? "" : "()V";
		
		if(method.name.equals(playerTickName)&&method.desc.equals(playerTickDescripter))
		{
			for(AbstractInsnNode instr : method.instructions.toArray())
			{
				if(instr.getOpcode()==ALOAD && ((VarInsnNode)instr).var == 0)
				{
					if(instr.getNext().getOpcode() == ALOAD && ((VarInsnNode)instr.getNext()).var == 2)
					{
						target = instr;
					}
				}
			}
		}
	}

	private void handleTick(MethodNode method, AbstractInsnNode target)
	{
		for(int i=0;i<3;i++)
		{
			target = target.getNext();
			method.instructions.remove(target.getPrevious());
		}
	}
} 
